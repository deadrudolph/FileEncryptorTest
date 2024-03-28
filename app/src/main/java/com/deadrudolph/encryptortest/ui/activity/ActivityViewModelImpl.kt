package com.deadrudolph.encryptortest.ui.activity

import com.deadrudolph.encryptortest.R
import com.deadrudolph.encryptortest.domain.model.FileModel
import com.deadrudolph.encryptortest.encryptor.Encryptor
import com.deadrudolph.encryptortest.ui.model.FileToSave
import com.deadrudolph.encryptortest.ui.model.Files
import java.io.File
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber


class ActivityViewModelImpl(
    private val encryptor: Encryptor
) : ActivityViewModel() {

    override val fileToSaveState = MutableStateFlow<FileToSave?>(null)
    override val filesState = MutableStateFlow(Files(listOf()))
    override val saveFileDialogState = MutableStateFlow<FileToSave?>(null)

    private var fileToDecrypt: FileModel? = null

    override fun onFileCopiedFromExternalStorage(filePath: String, fileName: String) {
        filesState.value = filesState.value.run {
            copy(
                files = files + FileModel(
                    id = System.currentTimeMillis(),
                    name = fileName,
                    path = filePath,
                    isEncrypted = false
                )
            )
        }
    }

    override fun onKeyCopiedFromExternalStorage(keyPath: String, filesDir: String) {
        fileToDecrypt?.also { file ->
            runCatching {
                val spec = PKCS8EncodedKeySpec(File(keyPath).readBytes())
                val rsaFact = KeyFactory.getInstance(RSA)
                val secretKey = rsaFact.generatePrivate(spec) as PrivateKey
                encryptor.decrypt(
                    file.path,
                    file.encryptedRSAKeyPath ?: run {
                        Timber.e("RSA key does not exist for file ${file.path}")
                        return
                    },
                    secretKey,
                    filesDir
                ) to file
            }.onFailure {
                Timber.e("Unable to get Secret key by path $keyPath")
            }.onSuccess { data ->
                File(data.second.path).delete()
                changeFileDecryptionStatus(data.second.id, data.first.filePath, false)
                fileToDecrypt = null
                fileToSaveState.value = FileToSave(
                    filePath = data.first.filePath,
                    messageId = R.string.file_save_message
                )
            }

        } ?: Timber.e("Unable to decrypt the file because it does not exist")
    }

    override fun deleteAllFiles() {
        filesState.value.files.forEach {
            File(it.path).delete()
            it.encryptedRSAKeyPath?.let { path -> File(path).delete() }
        }
    }

    override fun encryptFile(file: FileModel, filesDir: String) {
        val keys = KeyPairGenerator.getInstance("RSA").genKeyPair()
        val encryptionResult = encryptor.encrypt(
            filePath = file.path,
            filesDir = filesDir,
            publicRSAKey = keys.public
        )
        changeFileDecryptionStatus(
            id = file.id,
            newPath = encryptionResult.encryptedFile.absolutePath,
            isEncrypted = true,
            aesKeyPath = encryptionResult.encryptedAESKey.absolutePath
        )
        File(file.path).delete()
        val privateKeyPath = "$filesDir/${file.name}$RSA_EXTENSION"
        File(privateKeyPath).apply {
            writeBytes(keys.private.encoded)
        }

        fileToSaveState.value = FileToSave(
            filePath = privateKeyPath,
            messageId = R.string.key_save_message
        )
    }

    override fun deleteFile(file: FileModel) {
        val currentFilesList = filesState.value.files.toMutableList()
        val itemToDelete = currentFilesList.find { it.id == file.id } ?: return
        File(itemToDelete.path).delete()
        itemToDelete.encryptedRSAKeyPath?.let { path -> File(path).delete() }
        currentFilesList.remove(itemToDelete)
        filesState.value = Files(currentFilesList)
    }

    override fun decryptFile(file: FileModel) {
        fileToDecrypt = file
    }

    override fun showDialog(fileToSave: FileToSave?) {
        saveFileDialogState.value = fileToSave
    }

    override fun dismissDialog() {
        saveFileDialogState.value = null
    }

    private fun changeFileDecryptionStatus(
        id: Long,
        newPath: String,
        isEncrypted: Boolean,
        aesKeyPath: String? = null
    ) {
        filesState.value = Files(filesState.value.files.map { file ->
            if (file.id == id) {
                file.copy(
                    isEncrypted = isEncrypted,
                    path = newPath,
                    encryptedRSAKeyPath = aesKeyPath
                )
            } else file
        }
        )
    }

    private companion object {
        const val RSA = "RSA"
        const val RSA_EXTENSION = ".rsa"
    }
}

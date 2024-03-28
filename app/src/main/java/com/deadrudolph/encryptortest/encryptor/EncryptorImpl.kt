package com.deadrudolph.encryptortest.encryptor

import com.deadrudolph.encryptortest.encryptor.data.FileEncryptor
import com.deadrudolph.encryptortest.encryptor.key.KeyEncryptor
import com.deadrudolph.encryptortest.encryptor.model.DecryptedResult
import com.deadrudolph.encryptortest.encryptor.model.EncryptionResult
import java.security.PrivateKey
import java.security.PublicKey

class EncryptorImpl(
    private val fileEncryptor: FileEncryptor,
    private val keyEncryptor: KeyEncryptor
) : Encryptor {

    override fun encrypt(filePath: String, filesDir: String, publicRSAKey: PublicKey): EncryptionResult {
        val encryptedData = fileEncryptor.encryptFile(filePath, filesDir)
        val encryptedKey = keyEncryptor.encryptAESKey(encryptedData.key, filesDir, publicRSAKey)
        return EncryptionResult(
            encryptedAESKey = requireNotNull(encryptedKey),
            encryptedFile = encryptedData.encryptedFile,
        )
    }

    override fun decrypt(
        filePath: String,
        aesKeyFilePath: String,
        rsaKey: PrivateKey?,
        rootFilesPath: String
    ): DecryptedResult {
        val decryptedAESKey = keyEncryptor.decryptKey(
            keyPath = aesKeyFilePath,
            rsaKey = rsaKey,
            rootFilesPath = rootFilesPath
        )
        return fileEncryptor.decryptFile(
            filePath,
            decryptedAESKey,
            rootFilesPath
        )
    }
}

package com.deadrudolph.encryptortest.encryptor.data

import com.deadrudolph.encryptortest.encryptor.model.DecryptedResult
import com.deadrudolph.encryptortest.encryptor.model.EncryptedData
import java.io.File
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Uses simple symmetric encryption for a big amount of data
 * */
internal class FileEncryptorImpl(
    private val keyEncryptionSizeBit: Int = DEFAULT_KEY_ENCRYPTION_SIZE_BIT
) : FileEncryptor {

    private val ivSpec = IvParameterSpec(ByteArray(DEFAULT_IV_BIT))

    /**
     * Encrypts data using simple symmetric AES algorithm
     * */
    override fun encryptFile(filePath: String, rootFilesDir: String): EncryptedData {
        val file = File(filePath)
        val key = generateAESKey(keyEncryptionSizeBit)
        val cipher = Cipher.getInstance(CIPHER_CONFIG_AES)
        val secretKeySpec = SecretKeySpec(key.encoded, AES)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)
        val encryptedFile = File("$rootFilesDir/${file.name}${ENCRYPTED_FILE_EXT}")
        val encryptedBytes = cipher.doFinal(file.readBytes())
        encryptedFile.writeBytes(encryptedBytes)
        return EncryptedData(
            encryptedFile = encryptedFile, key = key
        )
    }

    /**
     * Decrypts data using simple symmetric AES algorithm
     * */
    override fun decryptFile(
        filePath: String,
        aesKey: SecretKey,
        rootFilesPath: String
    ): DecryptedResult {
        val decryptedData = decryptAESData(filePath, aesKey)
        return File(filePath.removeAESExtension()).run {
            if (exists()) deleteRecursively()
            writeBytes(decryptedData)
            DecryptedResult(absolutePath)
        }
    }

    private fun decryptAESData(filePath: String, aesKey: SecretKey): ByteArray {
        val encryptedAesData = File(filePath).readBytes()
        val raw = aesKey.encoded
        val keySpec = SecretKeySpec(raw, AES)
        val cipher: Cipher = Cipher.getInstance(CIPHER_CONFIG_AES)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(encryptedAesData)
    }

    /**
     * @return Secret AES key for symmetric encryption
     * */
    private fun generateAESKey(keyEncryptionSizeBit: Int): SecretKey {
        return KeyGenerator.getInstance(AES).run {
            init(keyEncryptionSizeBit)
            generateKey()
        }
    }

    private fun String.removeAESExtension(): String {
        return replace(ENCRYPTED_FILE_EXT, "")
    }

    private companion object {
        const val AES = "AES"
        const val DEFAULT_IV_BIT = 16
        const val DEFAULT_KEY_ENCRYPTION_SIZE_BIT = 128
        const val CIPHER_CONFIG_AES = "AES/CTR/NoPadding"
        const val ENCRYPTED_FILE_EXT = ".aes"
    }
}

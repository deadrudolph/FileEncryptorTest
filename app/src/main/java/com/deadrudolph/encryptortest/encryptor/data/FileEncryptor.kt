package com.deadrudolph.encryptortest.encryptor.data

import com.deadrudolph.encryptortest.encryptor.model.DecryptedResult
import com.deadrudolph.encryptortest.encryptor.model.EncryptedData
import javax.crypto.SecretKey

/**
 * File encryptor interface
 * */
interface FileEncryptor {

    /**
     * Encrypts the given file
     * @param filePath the pth of the file which needs to be encrypted
     * @param rootFilesDir path where the encrypted filed should be stored
     * */
    fun encryptFile(filePath: String, rootFilesDir: String): EncryptedData

    /**
     * @param filePath Encrypted file which needs to be decrypted
     * @param aesKey Decrypted AES key
     * @param rootFilesPath Path where decrypted files should be stored
     * @return decrypted file
     */
    fun decryptFile(
        filePath: String,
        aesKey: SecretKey,
        rootFilesPath: String
    ): DecryptedResult
}

package com.deadrudolph.encryptortest.encryptor

import com.deadrudolph.encryptortest.encryptor.model.DecryptedResult
import com.deadrudolph.encryptortest.encryptor.model.EncryptionResult
import java.security.PrivateKey
import java.security.PublicKey

interface Encryptor {

    fun encrypt(filePath: String, filesDir: String, publicRSAKey: PublicKey): EncryptionResult

    /**
     * @param filePath Encrypted file which needs to be decrypted
     * @param aesKeyFilePath The path of the encrypted AES key which should be decrypted and used to decrypt the file
     * @param rsaKey Private RSA key to decrypt AES key
     * @param rootFilesPath Path where decrypted files should be stored
     * @return decrypted file
     */
    fun decrypt(
        filePath: String,
        aesKeyFilePath: String,
        rsaKey: PrivateKey?,
        rootFilesPath: String
    ): DecryptedResult
}

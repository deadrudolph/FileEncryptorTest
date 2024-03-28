package com.deadrudolph.encryptortest.encryptor.key

import java.io.File
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey

interface KeyEncryptor {

    /** Returns File which contains encrypted AES key */
    fun encryptAESKey(key: SecretKey, filesDir: String, publicRSAKey: PublicKey): File?

    /**
     * @param keyPath Encrypted key which needs to be decrypted
     * @param rsaKey Private RSA key to decrypt AES key
     * @param rootFilesPath Path where decrypted files should be stored
     * @return decrypted file
     */
    fun decryptKey(
        keyPath: String,
        rsaKey: PrivateKey?,
        rootFilesPath: String
    ): SecretKey
}

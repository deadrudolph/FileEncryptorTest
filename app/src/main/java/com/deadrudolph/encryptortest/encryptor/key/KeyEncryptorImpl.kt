package com.deadrudolph.encryptortest.encryptor.key

import com.deadrudolph.encryptortest.encryptor.utils.multiCatch
import java.io.File
import java.security.InvalidKeyException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import timber.log.Timber

class KeyEncryptorImpl : KeyEncryptor {

    private val rsaCipher = Cipher.getInstance(CIPHER_CONFIG)

    /** Encrypts AES key using open RSA key*/
    override fun encryptAESKey(key: SecretKey, filesDir: String, publicRSAKey: PublicKey): File? {
        return { encryptSecretKey(key, filesDir, publicRSAKey) }.multiCatch(
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            IllegalStateException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class,
            KeyStoreException::class
        ) { message ->
            Timber.e(message ?: "Something went wrong while encryption of AES key")
            null
        }
    }

    override fun decryptKey(
        keyPath: String,
        rsaKey: PrivateKey?,
        rootFilesPath: String,
    ): SecretKey {
        val aesKey = File(keyPath)
        val encryptedAesKey = aesKey.readBytes()
        rsaCipher.init(Cipher.DECRYPT_MODE, rsaKey)
        val secretKeyBytes: ByteArray = rsaCipher.doFinal(encryptedAesKey)
        return SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.size, AES)
    }

    private fun encryptSecretKey(
        secretKey: SecretKey,
        filesDir: String,
        publicKey: PublicKey
    ): File {
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return File("$filesDir/$KEY_NAME$KEY_EXT").apply {
            writeBytes(rsaCipher.doFinal(secretKey.encoded))
        }
    }

    private companion object {
        const val CIPHER_CONFIG = "RSA/ECB/PKCS1Padding"
        const val RSA = "RSA"
        const val AES = "AES"
        const val KEY_NAME = "EncryptedKey"
        const val KEY_EXT = ".enc"
    }
}

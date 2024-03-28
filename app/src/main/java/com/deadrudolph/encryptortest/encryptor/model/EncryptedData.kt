package com.deadrudolph.encryptortest.encryptor.model

import java.io.File
import javax.crypto.SecretKey

/**
 * Contains encrypted file and key
 * */
data class EncryptedData(
    val encryptedFile: File,
    val key: SecretKey
)

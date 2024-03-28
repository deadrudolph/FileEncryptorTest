package com.deadrudolph.encryptortest.encryptor.model

import java.io.File

data class EncryptionResult(
    val encryptedFile: File,
    val encryptedAESKey: File
)

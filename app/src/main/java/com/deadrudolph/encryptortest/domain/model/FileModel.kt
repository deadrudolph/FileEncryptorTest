package com.deadrudolph.encryptortest.domain.model

data class FileModel(
    val id: Long,
    val name: String,
    val path: String,
    val isEncrypted: Boolean,
    val encryptedRSAKeyPath: String? = null
)

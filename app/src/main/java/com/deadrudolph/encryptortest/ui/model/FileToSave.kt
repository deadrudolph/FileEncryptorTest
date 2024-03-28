package com.deadrudolph.encryptortest.ui.model

import androidx.annotation.StringRes

data class FileToSave(
    val filePath: String,
    @StringRes val messageId: Int
)

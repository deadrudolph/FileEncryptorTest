package com.deadrudolph.encryptortest.ui.model

import androidx.compose.runtime.Immutable
import com.deadrudolph.encryptortest.domain.model.FileModel

@Immutable
data class Files(
    val files: List<FileModel>
)

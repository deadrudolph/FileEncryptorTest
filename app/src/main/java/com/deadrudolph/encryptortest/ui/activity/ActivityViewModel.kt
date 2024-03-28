package com.deadrudolph.encryptortest.ui.activity

import androidx.lifecycle.ViewModel
import com.deadrudolph.encryptortest.domain.model.FileModel
import com.deadrudolph.encryptortest.ui.model.FileToSave
import com.deadrudolph.encryptortest.ui.model.Files
import kotlinx.coroutines.flow.StateFlow

abstract class ActivityViewModel : ViewModel() {
    abstract val fileToSaveState: StateFlow<FileToSave?>
    abstract val filesState: StateFlow<Files>
    abstract val saveFileDialogState: StateFlow<FileToSave?>

    abstract fun onFileCopiedFromExternalStorage(filePath: String, fileName: String)
    abstract fun onKeyCopiedFromExternalStorage(keyPath: String, filesDir: String)
    abstract fun deleteAllFiles()
    abstract fun encryptFile(file: FileModel, filesDir: String)
    abstract fun deleteFile(file: FileModel)
    abstract fun decryptFile(file: FileModel)
    abstract fun showDialog(fileToSave: FileToSave?)
    abstract fun dismissDialog()
}

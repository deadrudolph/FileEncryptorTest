package com.deadrudolph.encryptortest.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.deadrudolph.encryptortest.file_reader.FileReader
import com.deadrudolph.encryptortest.ui.composable.AddButton
import com.deadrudolph.encryptortest.ui.composable.FileItem
import com.deadrudolph.encryptortest.ui.composable.SaveFileDialog
import com.deadrudolph.encryptortest.ui.theme.EncryptorTestTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private val fileReader by inject<FileReader>()

    private val viewModel by viewModel<ActivityViewModel>()

    private val readFileActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.data?.let { uri ->
            fileReader.readFile(contentResolver, uri, filesDir.absolutePath).also { file ->
                viewModel.onFileCopiedFromExternalStorage(file.absolutePath, file.name)
            }
        }
    }

    private val readKeyActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.data?.let { uri ->
            fileReader.readFile(contentResolver, uri, filesDir.absolutePath).also { file ->
                viewModel.onKeyCopiedFromExternalStorage(file.absolutePath, filesDir.absolutePath)
            }
        }
    }

    private val writeFileActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.data?.let { uri ->
            viewModel.fileToSaveState.value?.let { fileToWrite ->
                fileReader.writeFile(contentResolver, uri, fileToWrite.filePath)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        setContent {
            EncryptorTestTheme(dynamicColor = false) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onBackground
                ) {

                    val isSaveDialogShownState = viewModel.saveFileDialogState.collectAsState()

                    isSaveDialogShownState.value?.let { state ->
                        SaveFileDialog(
                            modifier = Modifier
                                .wrapContentHeight()
                                .width(300.dp)
                                .background(Color.Gray),
                            message = stringResource(id = state.messageId)
                        ) { fileName ->
                            openFileSaver(fileName.ifBlank { "Undefined" })
                            viewModel.dismissDialog()
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        AddButton(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(20.dp)
                                .size(50.dp, 50.dp),
                            onClick = {
                                openFileChooser(readFileActivityResult)
                            }
                        )
                        val filesState = viewModel.filesState.collectAsState()

                        LazyColumn(
                            contentPadding = PaddingValues(5.dp)
                        ) {
                            items(
                                items = filesState.value.files,
                                key = { it.id }
                            ) { item ->
                                FileItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentHeight()
                                        .padding(top = 5.dp)
                                        .background(Color.Gray, RoundedCornerShape(15.dp)),
                                    fileItem = item,
                                    onEncryptClicked = {
                                        viewModel.encryptFile(
                                            item,
                                            filesDir.absolutePath
                                        )
                                    },
                                    onDeleteClicked = { viewModel.deleteFile(item) },
                                    onDecryptClicked = {
                                        openFileChooser(readKeyActivityResult)
                                        viewModel.decryptFile(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        // This app is just a snippet. For now not supporting saving files to DB,
        // so it's needed to sweep all the files
        viewModel.deleteAllFiles()
        super.onDestroy()
    }

    private fun openFileSaver(fileName: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/*"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        writeFileActivityResult.launch(intent)
    }

    private fun openFileChooser(launcher: ActivityResultLauncher<Intent?>) {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.setType("*/*")
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        launcher.launch(chooseFile)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.fileToSaveState.collect { fileToWrite ->
                viewModel.showDialog(fileToWrite)
            }
        }
    }
}

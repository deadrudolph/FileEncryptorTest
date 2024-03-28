package com.deadrudolph.encryptortest.file_reader

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream

class FileReaderImpl : FileReader {

    override fun readFile(contentResolver: ContentResolver, uri: Uri, filesDir: String): File {
        val fileName = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        ).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                cursor.getString(
                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME).takeIf { it > 0 } ?: 0
                )
            } else DEFAULT_FILE_NAME
        }
        val os = contentResolver.openInputStream(uri)
        val array = os?.readBytes() ?: ByteArray(DEFAULT_BYTES)
        val newFile = File(filesDir, fileName ?: DEFAULT_FILE_NAME).apply {
            if (exists()) delete()
            writeBytes(array)
        }
        return newFile
    }

    override fun writeFile(contentResolver: ContentResolver, uri: Uri, filePath: String) {
        val os = contentResolver.openOutputStream(uri)
        val fileInputStream = FileInputStream(File(filePath))
        val byteArray = ByteArray(DEFAULT_BYTES)
        var length: Int
        while ((fileInputStream.read(byteArray)).apply {
                length = this
            } > 0) {
            os?.write(byteArray, 0, length);
        }
        os?.close()
    }

    private companion object {
        const val DEFAULT_BYTES = 1024
        const val DEFAULT_FILE_NAME = "Undefined"
    }
}

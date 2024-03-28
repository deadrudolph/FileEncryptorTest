package com.deadrudolph.encryptortest.file_reader

import android.content.ContentResolver
import android.net.Uri
import java.io.File

interface FileReader {

    /** @return file path in internal storage */
    fun readFile(contentResolver: ContentResolver, uri: Uri, filesDir: String): File

    /** Writes a file from internal storage to external storage*/
    fun writeFile(contentResolver: ContentResolver, uri: Uri, filePath: String)
}

package com.deadrudolph.encryptortest.di.module

import com.deadrudolph.encryptortest.file_reader.FileReader
import com.deadrudolph.encryptortest.file_reader.FileReaderImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val FileReaderModule = module {

    factory { FileReaderImpl() } bind FileReader::class
}

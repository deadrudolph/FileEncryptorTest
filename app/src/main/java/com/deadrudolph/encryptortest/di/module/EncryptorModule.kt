package com.deadrudolph.encryptortest.di.module

import com.deadrudolph.encryptortest.encryptor.Encryptor
import com.deadrudolph.encryptortest.encryptor.EncryptorImpl
import com.deadrudolph.encryptortest.encryptor.data.FileEncryptor
import com.deadrudolph.encryptortest.encryptor.data.FileEncryptorImpl
import com.deadrudolph.encryptortest.encryptor.key.KeyEncryptor
import com.deadrudolph.encryptortest.encryptor.key.KeyEncryptorImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val EncryptorModule = module {
    factory { FileEncryptorImpl() } bind FileEncryptor::class
    factory { KeyEncryptorImpl() } bind KeyEncryptor::class
    factory { EncryptorImpl(get(), get()) } bind Encryptor::class
}

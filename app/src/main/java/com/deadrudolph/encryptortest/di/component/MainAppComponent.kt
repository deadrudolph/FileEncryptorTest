package com.deadrudolph.encryptortest.di.component

import com.deadrudolph.encryptortest.di.module.EncryptorModule
import com.deadrudolph.encryptortest.di.module.FileReaderModule
import com.deadrudolph.encryptortest.di.module.ViewModelModule
import org.koin.core.module.Module

object MainAppComponent : KoinComponent {

    override fun getModules(): List<Module> = listOf(
        EncryptorModule,
        ViewModelModule,
        FileReaderModule
    )

}

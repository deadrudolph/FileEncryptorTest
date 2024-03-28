package com.deadrudolph.encryptortest.di.component

import org.koin.core.module.Module

interface KoinComponent {

    fun getModules(): List<Module>
}

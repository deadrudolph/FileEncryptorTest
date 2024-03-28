package com.deadrudolph.encryptortest

import android.app.Application
import com.deadrudolph.encryptortest.di.component.MainAppComponent
import com.deadrudolph.encryptortest.logging.EncryptorDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                MainAppComponent.getModules()
            )
        }
    }

    private fun initTimber() {
        Timber.plant(EncryptorDebugTree())
    }

}

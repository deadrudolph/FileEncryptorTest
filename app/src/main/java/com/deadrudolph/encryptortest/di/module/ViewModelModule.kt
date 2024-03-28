package com.deadrudolph.encryptortest.di.module

import com.deadrudolph.encryptortest.ui.activity.ActivityViewModel
import com.deadrudolph.encryptortest.ui.activity.ActivityViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel { ActivityViewModelImpl(get()) } bind ActivityViewModel::class
}

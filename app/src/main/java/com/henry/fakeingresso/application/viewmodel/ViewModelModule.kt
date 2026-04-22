package com.henry.fakeingresso.application.viewmodel

import com.henry.fakeingresso.home.viewmodel.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ViewModelModule {

    fun getModules() = viewModel

    private val viewModel = module {
        singleOf(::HomeViewModel)
    }
}
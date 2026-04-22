package com.henry.fakeingresso.application.modules

import com.henry.fakeingresso.application.viewmodel.ViewModelModule
import com.henry.fakeingresso.repository.local.LocalModule
import com.henry.fakeingresso.repository.remote.networkModule
import com.henry.fakeingresso.usecase.UseCaseModule

object ConfigureModule {
    fun getModules() = listOf(
        networkModule,
        LocalModule.getModule(),
        ViewModelModule.getModules(),
        RepositoryModule.getModules(),
        UseCaseModule.getModules()
    )
}
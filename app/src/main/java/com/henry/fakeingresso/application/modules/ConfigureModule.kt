package com.henry.fakeingresso.application.modules

import com.henry.fakeingresso.repository.remote.networkModule

object ConfigureModule {
    fun getModules() = listOf(
        networkModule,
    )
}
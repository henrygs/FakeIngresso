package com.henry.fakeingresso.application

import android.app.Application
import com.henry.fakeingresso.application.modules.ConfigureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(ConfigureModule.getModules())
        }
    }
}
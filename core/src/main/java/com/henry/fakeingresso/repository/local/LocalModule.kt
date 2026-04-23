package com.henry.fakeingresso.repository.local

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object LocalModule {

    fun getModule() = databaseModule

    val databaseModule = module {
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "fakeingresso_db"
            ).fallbackToDestructiveMigration()
                .build()
        }

        single { get<AppDatabase>().movieDao() }
        single { get<AppDatabase>().favoriteDao() }
    }
}
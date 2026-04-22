package com.henry.fakeingresso.usecase

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object UseCaseModule {
    fun getModules() = useCaseModule
    private val useCaseModule = module {
        singleOf(::GetMoviesUseCaseImpl) { bind<GetMoviesUseCase>() }
    }
}
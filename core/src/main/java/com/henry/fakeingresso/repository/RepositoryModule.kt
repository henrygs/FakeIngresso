import com.henry.fakeingresso.repository.GetMoviesRepository
import com.henry.fakeingresso.repository.GetMoviesRepositoryImpl

import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf

import org.koin.dsl.module

object RepositoryModule {
    fun getModules(): Module = repositoryModule

    private val repositoryModule = module {
        singleOf(::GetMoviesRepositoryImpl) { bind<GetMoviesRepository>() }
    }
}

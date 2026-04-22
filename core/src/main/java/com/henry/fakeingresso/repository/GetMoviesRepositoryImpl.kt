package com.henry.fakeingresso.repository

import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.domain.model.toDTO
import com.henry.fakeingresso.repository.local.MovieDao
import com.henry.fakeingresso.repository.remote.ApiService
import kotlinx.coroutines.flow.Flow

class GetMoviesRepositoryImpl(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) : GetMoviesRepository {
    override fun getMovies(): Flow<List<MovieDTO>> = movieDao.getAllMovies()

    override suspend fun getMovieById(id: String): MovieDTO? = movieDao.getMovieById(id)

    override suspend fun refreshMovies(): Result<Unit> {
        return try {
            val remote = apiService.getMovies()
            movieDao.deleteAllMovies()
            movieDao.insertAllMovies(remote.items.map { it.toDTO() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
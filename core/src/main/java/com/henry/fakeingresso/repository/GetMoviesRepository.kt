package com.henry.fakeingresso.repository

import com.henry.fakeingresso.domain.model.MovieDTO
import kotlinx.coroutines.flow.Flow

interface GetMoviesRepository {
    fun getMovies() : Flow<List<MovieDTO>>
    suspend fun refreshMovies() : Result<Unit>
}
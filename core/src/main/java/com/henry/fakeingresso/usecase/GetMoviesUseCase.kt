package com.henry.fakeingresso.usecase

import com.henry.fakeingresso.domain.model.MovieDTO
import kotlinx.coroutines.flow.Flow

interface GetMoviesUseCase {
    operator fun invoke() : Flow<List<MovieDTO>>
    suspend fun refreshMovies() : Result<Unit>
}
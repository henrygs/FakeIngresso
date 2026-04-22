package com.henry.fakeingresso.usecase

import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.repository.GetMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMoviesUseCaseImpl(
    private val repository: GetMoviesRepository
): GetMoviesUseCase {
    override fun invoke(): Flow<List<MovieDTO>> {
        val movies = repository.getMovies().map {
            it.sortedBy { movie -> movie.premiereDate.localDate}
        }
        return movies
    }

    override suspend fun refreshMovies(): Result<Unit> =
        repository.refreshMovies()
}
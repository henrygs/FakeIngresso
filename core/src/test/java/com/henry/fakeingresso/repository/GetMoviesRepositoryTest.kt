package com.henry.fakeingresso.repository

import com.henry.fakeingresso.domain.model.Movie
import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.domain.model.MoviesResponse
import com.henry.fakeingresso.domain.model.PremiereDate
import com.henry.fakeingresso.repository.local.MovieDao
import com.henry.fakeingresso.repository.remote.ApiService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetMoviesRepositoryTest {
    private lateinit var repository: GetMoviesRepositoryImpl
    private val apiService: ApiService = mockk()
    private val movieDao: MovieDao = mockk()

    @Before
     fun setupTest() {
        repository = GetMoviesRepositoryImpl(apiService, movieDao)
    }

    @Test
    fun `should return failure when api throws exception`() = runTest {
        // Arrange
        coEvery { apiService.getMovies() } throws RuntimeException("Erro de rede")

        // Act
        val result = repository.refreshMovies()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Erro de rede", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { movieDao.deleteAllMovies() }
        coVerify(exactly = 0) { movieDao.insertAllMovies(any()) }
    }

    @Test
    fun `should refresh movies successfully`() = runTest {
        // Arrange
        val movies = listOf(movie(), movie())
        val response = MoviesResponse( count = 1, items = movies)

        coEvery { apiService.getMovies() } returns response
        coEvery { movieDao.deleteAllMovies() } just Runs
        coEvery { movieDao.insertAllMovies(any()) } just Runs

        // Act
        val result = repository.refreshMovies()

        // Assert
        assertTrue(result.isSuccess)
        coVerifyOrder {
            apiService.getMovies()
            movieDao.deleteAllMovies()
            movieDao.insertAllMovies(any())
        }
    }


    @Test
    fun `should return movies from dao`() = runTest {
        //arrange
        val movies = listOf(movieDTO(title = "1"), movieDTO(title = "2"))
        every { movieDao.getAllMovies() } returns flowOf(movies)

        //act
        val result = repository.getMovies()

        //assert
        assertEquals(movies, result.first())
        verify { movieDao.getAllMovies() }
    }

    private fun movieDTO(title: String) =  MovieDTO(
        id = "1",
        title = "Filme $title",
        originalTitle = "",
        synopsis = "",
        cast = "",
        director = "",
        duration = "",
        genres = emptyList(),
        contentRating = "",
        rating = 0.0,
        premiereDate = PremiereDate("", "", "", false, "", ""),
        images = emptyList(),
        imageFeatured = "",
        trailers = emptyList()
    )
    private fun movie() = Movie(
        id = "2",
        title = "Filme 2",
        originalTitle = "",
        synopsis = "",
        cast = "",
        director = "",
        duration = "",
        genres = emptyList(),
        contentRating = "",
        rating = 0.0,
        premiereDate = PremiereDate("", "", "", false, "", ""),
        images = emptyList(),
        imageFeatured = "",
        trailers = emptyList(),
    )
}
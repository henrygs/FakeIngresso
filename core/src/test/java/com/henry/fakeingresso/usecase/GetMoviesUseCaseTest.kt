package com.henry.fakeingresso.usecase

import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.domain.model.PremiereDate
import com.henry.fakeingresso.repository.GetMoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class GetMoviesUseCaseTest {

    private lateinit var useCase: GetMoviesUseCaseImpl
    private val repository: GetMoviesRepository = mockk()

    @Before
    fun setupTest() {
        useCase = GetMoviesUseCaseImpl(repository)
    }

    @Test
    fun `should return failure when refresh movies fails`() = runTest {
        coEvery { repository.refreshMovies() } returns Result.failure(Exception("Erro"))

        val result = useCase.refreshMovies()

        assertEquals(true, result.isFailure)

    }

    @Test
    fun `should return movies refresh success`() = runTest {
        coEvery { repository.refreshMovies() } returns Result.success(Unit)
        val result = useCase.refreshMovies()
        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `should return null when there is no movie`() = runTest {
        coEvery { repository.getMovieById("1") } returns null
        val result = useCase.getMovieById("1")
        assertEquals(null, result)
    }

    @Test
    fun `should return a movie when passing an id`() = runTest {
        val movie = createMovie("1", "2024-06-01")
        coEvery { repository.getMovieById("1") } returns movie
        val result = useCase.getMovieById("1")
        assertEquals(movie, result)
    }

    @Test
    fun `should return an empty list when an empty list arrives`() = runTest {
        // Given
        coEvery { repository.getMovies() } returns flowOf(emptyList())

        // When
        val result = useCase().first()

        // Then
        assertEquals(emptyList<MovieDTO>(), result)
    }

    @Test
    fun `should return ordered movies when by data`() = runTest {
        // Given
        val movies = listOf(
            createMovie("1", "2024-06-01"),
            createMovie("2", "2024-05-01"),
            createMovie("3", "2024-07-01")
        )
        coEvery { repository.getMovies() } returns flowOf(movies)

        // When
        val result = useCase().first()

        // Then
        assertEquals("2", result[0].id)
        assertEquals("1", result[1].id)
        assertEquals("3", result[2].id)
    }

    private fun createMovie(id: String, localDate: String): MovieDTO {
        return MovieDTO(
            id = id,
            title = "Movie $id",
            originalTitle = "Original Movie $id",
            synopsis = "",
            cast = "",
            director = "",
            duration = "",
            genres = emptyList(),
            contentRating = "",
            rating = 0.0,
            premiereDate = PremiereDate(
                dayAndMonth = "", dayOfWeek = "", hour = "",
                isToday = false, localDate = localDate, year = ""
            ),
            images = emptyList(),
            imageFeatured = "",
            trailers = emptyList()
        )
    }
}
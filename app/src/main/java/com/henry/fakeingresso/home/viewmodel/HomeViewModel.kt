package com.henry.fakeingresso.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    private var searchQuery = ""

    init {
        observeMovies()
    }

    private fun observeMovies() {
        viewModelScope.launch {
            getMoviesUseCase()
                .catch { e -> _uiState.value = HomeUiState.Error(e.message ?: UNKNOWN_ERROR) }
                .collect { movies ->
                    if (movies.isEmpty()) {
                        _uiState.value = HomeUiState.Empty
                    } else {
                        _uiState.value = HomeUiState.Success(
                            movies = movies,
                            topMovies = movies.take(TOP_MOVIES_COUNT),
                            filteredMovies = filterMovies(movies, searchQuery),
                            searchQuery = searchQuery
                        )
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        val currentState = _uiState.value
        if (currentState is HomeUiState.Success) {
            _uiState.value = currentState.copy(
                searchQuery = query,
                filteredMovies = filterMovies(currentState.movies, query)
            )
        }
    }

    fun refreshMovies(isConnected: Boolean) {
        if (!isConnected) {
            if (_uiState.value !is HomeUiState.Success) {
                _uiState.value = HomeUiState.Error(NO_CONNECTION_ERROR)
            }
            return
        }
        viewModelScope.launch {
            getMoviesUseCase.refreshMovies().onFailure { e ->
                if (_uiState.value !is HomeUiState.Success) {
                    _uiState.value = HomeUiState.Error(e.message ?: UNKNOWN_ERROR)
                }
            }
        }
    }

    private fun filterMovies(movies: List<MovieDTO>, query: String): List<MovieDTO> {
        if (query.isBlank()) return movies
        return movies.filter { movie ->
            movie.title.contains(query, ignoreCase = true) ||
                    movie.originalTitle.contains(query, ignoreCase = true) ||
                    movie.director.contains(query, ignoreCase = true) ||
                    movie.genres.any { it.contains(query, ignoreCase = true) }
        }
    }

    private companion object {
        const val TOP_MOVIES_COUNT = 10
        const val UNKNOWN_ERROR = "Erro desconhecido"
        const val NO_CONNECTION_ERROR = "Sem conexão com a internet"
    }
}

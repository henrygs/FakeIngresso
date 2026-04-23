package com.henry.fakeingresso.home.viewmodel

import com.henry.fakeingresso.domain.model.MovieDTO

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data object Empty : HomeUiState()
    data class Success(
        val movies: List<MovieDTO>,
        val topMovies: List<MovieDTO>,
        val filteredMovies: List<MovieDTO> = movies,
        val searchQuery: String = "",
        val favoriteIds: Set<String> = emptySet()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
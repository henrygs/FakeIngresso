package com.henry.fakeingresso.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.repository.local.MovieDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(
    movieDao: MovieDao
) : ViewModel() {

    val favoriteMovies: StateFlow<List<MovieDTO>> = movieDao.getFavoriteMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

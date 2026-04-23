package com.henry.fakeingresso.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henry.fakeingresso.domain.model.MovieDTO
import com.henry.fakeingresso.repository.local.FavoriteDao
import com.henry.fakeingresso.repository.local.FavoriteEntity
import com.henry.fakeingresso.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    private val _movie = MutableStateFlow<MovieDTO?>(null)
    val movie: StateFlow<MovieDTO?> = _movie

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadMovie(movieId: String) {
        viewModelScope.launch {
            _movie.value = getMoviesUseCase.getMovieById(movieId)
        }
        viewModelScope.launch {
            favoriteDao.isFavorite(movieId).collectLatest { _isFavorite.value = it }
        }
    }

    fun toggleFavorite() {
        val movieId = _movie.value?.id ?: return
        viewModelScope.launch {
            if (_isFavorite.value) {
                favoriteDao.removeFavorite(movieId)
            } else {
                favoriteDao.addFavorite(FavoriteEntity(movieId))
            }
        }
    }
}

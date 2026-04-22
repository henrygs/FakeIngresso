package com.henry.fakeingresso.domain.model

data class MoviesResponse(
    val count: Int,
    val items: List<Movie>
)
package com.henry.fakeingresso.repository.remote

import MoviesResponse
import retrofit2.http.GET

interface ApiService {

    @GET("v0/events/coming-soon/partnership/desafio")
    suspend fun getComingSoonEvents(): MoviesResponse
}

package com.henry.fakeingresso.home.extations

import com.henry.fakeingresso.domain.model.MovieDTO

val MovieDTO.horizontalPosterUrl: String
    get() = images.firstOrNull { it.type == "PosterHorizontal" }?.url
        ?: images.firstOrNull { it.type == "PosterPortrait" }?.url
        ?: imageFeatured

val MovieDTO.portraitPosterUrl: String
    get() = images.firstOrNull { it.type == "PosterPortrait" }?.url
        ?: images.firstOrNull { it.type == "PosterHorizontal" }?.url
        ?: imageFeatured

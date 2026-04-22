package com.henry.fakeingresso.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieDTO(
    @PrimaryKey
    val id: String,
    val title: String,
    val originalTitle: String,
    val synopsis: String,
    val cast: String,
    val director: String,
    val duration: String,
    val genres: List<String>,
    val contentRating: String,
    val rating: Double,
    val premiereDate: PremiereDate,
    val images: List<Image>,
    val imageFeatured: String,
    val trailers: List<Trailer>
)

fun Movie.toDTO() = MovieDTO(
    id = id,
    title = title,
    originalTitle = originalTitle,
    synopsis = synopsis,
    cast = cast,
    director = director,
    duration = duration,
    genres = genres,
    contentRating = contentRating,
    rating = rating,
    premiereDate = premiereDate,
    images = images,
    imageFeatured = imageFeatured,
    trailers = trailers
)

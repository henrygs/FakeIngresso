package com.henry.fakeingresso.domain.model

data class PremiereDate(
    val dayAndMonth: String,
    val dayOfWeek: String,
    val hour: String,
    val isToday: Boolean,
    val localDate: String,
    val year: String
)
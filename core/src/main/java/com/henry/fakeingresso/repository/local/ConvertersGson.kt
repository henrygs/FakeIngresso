package com.henry.fakeingresso.repository.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.henry.fakeingresso.domain.model.Image
import com.henry.fakeingresso.domain.model.PremiereDate
import com.henry.fakeingresso.domain.model.Trailer

class ConvertersGson {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromImageList(value: List<Image>): String = gson.toJson(value)

    @TypeConverter
    fun toImageList(value: String): List<Image> {
        val type = object : TypeToken<List<Image>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromTrailerList(value: List<Trailer>): String = gson.toJson(value)

    @TypeConverter
    fun toTrailerList(value: String): List<Trailer> {
        val type = object : TypeToken<List<Trailer>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromPremiereDate(value: PremiereDate): String = gson.toJson(value)

    @TypeConverter
    fun toPremiereDate(value: String): PremiereDate =
        gson.fromJson(value, PremiereDate::class.java)

}
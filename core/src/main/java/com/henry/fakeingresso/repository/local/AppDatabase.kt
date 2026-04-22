package com.henry.fakeingresso.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.henry.fakeingresso.domain.model.MovieDTO

@Database(
    entities = [MovieDTO::class, FavoriteEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(ConvertersGson::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
}

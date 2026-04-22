package com.henry.fakeingresso.repository.local


import com.henry.fakeingresso.domain.model.MovieDTO
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<MovieDTO>)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}
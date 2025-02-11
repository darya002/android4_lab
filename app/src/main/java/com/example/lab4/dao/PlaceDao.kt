package com.example.lab4.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lab4.entities.Place

import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Insert
    suspend fun insertPlace(place: Place)

    @Update
    suspend fun updatePlace(place: Place)

    @Delete
    suspend fun deletePlace(place: Place)

    @Query("SELECT * FROM place")
    fun getAllPlaces(): Flow<List<Place>>

    @Query("SELECT * FROM place WHERE id = :id")
    suspend fun getPlaceById(id: Long): Place  // Новый метод для получения места по ID

    @Query("UPDATE place SET visited = :visited WHERE id = :id")
    suspend fun updateVisited(id: Long, visited: Boolean)
}

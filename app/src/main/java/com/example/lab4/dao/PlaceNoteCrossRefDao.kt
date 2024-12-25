package com.example.lab4.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lab4.entities.PlaceNoteCrossRef

@Dao
interface PlaceNoteCrossRefDao {
    @Insert
    suspend fun insertPlaceNoteCrossRef(crossRef: PlaceNoteCrossRef)

    @Query("SELECT * FROM place_note_cross_ref")
    suspend fun getAllPlaceNoteCrossRefs(): List<PlaceNoteCrossRef>
}

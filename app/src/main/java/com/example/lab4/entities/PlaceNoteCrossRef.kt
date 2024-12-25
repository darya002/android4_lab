package com.example.lab4.entities

import androidx.room.Entity

@Entity(
    tableName = "place_note_cross_ref",
    primaryKeys = ["place_id", "note_id"]
)
data class PlaceNoteCrossRef(
    val place_id: Long,
    val note_id: Long
)

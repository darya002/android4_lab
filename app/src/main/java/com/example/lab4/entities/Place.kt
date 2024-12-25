package com.example.lab4.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")  // Аннотация для Room
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val location: String,
    val type: String
)

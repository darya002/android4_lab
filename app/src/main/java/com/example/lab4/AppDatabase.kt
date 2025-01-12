package com.example.lab4

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lab4.dao.NoteDao
import com.example.lab4.dao.PlaceDao
import com.example.lab4.dao.PlaceNoteCrossRefDao
import com.example.lab4.entities.Note
import com.example.lab4.entities.Place
import com.example.lab4.entities.PlaceNoteCrossRef

@Database(entities = [Place::class, Note::class, PlaceNoteCrossRef::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao
    abstract fun noteDao(): NoteDao
    abstract fun placeNoteCrossRefDao(): PlaceNoteCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "place_database"
                )
                    .fallbackToDestructiveMigration() // Удаление данных при изменении схемы
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
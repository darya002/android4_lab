package com.example.lab4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab4.dao.NoteDao
import com.example.lab4.dao.PlaceDao
import com.example.lab4.entities.Note
import com.example.lab4.entities.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()
    private val noteDao: NoteDao = AppDatabase.getDatabase(application).noteDao()

    // Используем Flow для списка достопримечательностей
    val allPlaces: Flow<List<Place>> = placeDao.getAllPlaces()

    // Используем Flow для списка заметок
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Методы для добавления, обновления и удаления данных
    fun addPlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.insertPlace(place)
        }
    }

    fun updatePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.updatePlace(place)
        }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.deletePlace(place)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }
}

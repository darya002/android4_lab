package com.example.lab4.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab4.AppDatabase
import com.example.lab4.dao.NoteDao
import com.example.lab4.dao.PlaceDao
import com.example.lab4.entities.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()
    private val noteDao: NoteDao = AppDatabase.getDatabase(application).noteDao()

    // StateFlow для списка мест
    private val _allPlaces = MutableStateFlow<List<Place>>(emptyList())
    val allPlaces: StateFlow<List<Place>> = _allPlaces

    init {
        // Инициализация данных
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.getAllPlaces().collect { places ->
                _allPlaces.value = places
            }
        }
    }

    // Методы добавления, обновления и удаления данных
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

    fun updateVisited(id: Long, visited: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.updateVisited(id, visited)
        }
    }
}

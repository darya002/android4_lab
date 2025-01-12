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

    private val _allPlaces = MutableStateFlow<List<Place>>(emptyList())
    val allPlaces: StateFlow<List<Place>> = _allPlaces

    init {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.getAllPlaces().collect { places ->
                _allPlaces.value = places
            }
        }
    }

    fun updateVisited(id: Long, visited: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.updateVisited(id, visited)
        }
    }
}

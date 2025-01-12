package com.example.lab4.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lab4.AppDatabase
import com.example.lab4.dao.PlaceDao
import com.example.lab4.entities.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()

    // LiveData для текущего места
    private val _place = MutableLiveData<Place?>()
    val place: LiveData<Place?> = _place

    // Установка текущего места
    fun setPlace(place: Place?) {
        _place.value = place
    }

    // Удаление текущего места
    fun deletePlace() {
        _place.value?.let { currentPlace ->
            viewModelScope.launch(Dispatchers.IO) {
                placeDao.deletePlace(currentPlace)
            }
        }
    }
}

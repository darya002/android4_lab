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

class AddEditPlaceViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()

    private val _place = MutableLiveData<Place?>()
    val place: LiveData<Place?> = _place

    fun setPlace(place: Place?) {
        _place.value = place
    }

    fun savePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            if (place.id == 0L) {
                placeDao.insertPlace(place) // Новое место
            } else {
                placeDao.updatePlace(place) // Обновление
            }
        }
    }
}

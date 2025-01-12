package com.example.lab4.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lab4.AppDatabase
import com.example.lab4.dao.PlaceDao
import com.example.lab4.entities.Note
import com.example.lab4.entities.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()

    private val _place = MutableLiveData<Place?>()
    val place: LiveData<Place?> get() = _place
    private val noteDao = AppDatabase.getDatabase(application).noteDao()

    fun setPlace(place: Place?) {
        _place.value = place
    }

    fun getNotesForPlace(placeId: Long): LiveData<List<Note>> {
        val notes = MutableLiveData<List<Note>>()
        CoroutineScope(Dispatchers.IO).launch {
            val placeNotes = noteDao.getNotesForPlace(placeId)
            notes.postValue(placeNotes)
        }
        return notes
    }
    fun deletePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            placeDao.deletePlace(place)
        }
    }
}

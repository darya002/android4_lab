package com.example.lab4.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lab4.AppDatabase
import com.example.lab4.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val _noteAdded = MutableLiveData<Boolean>()
    val noteAdded: LiveData<Boolean> get() = _noteAdded

    private val noteDao = AppDatabase.getDatabase(application).noteDao()

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(note)
            _noteAdded.postValue(true) // Уведомляем об успешном добавлении
        }
    }

    fun resetNoteAddedFlag() {
        _noteAdded.value = false
    }
}

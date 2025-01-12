package com.example.lab4.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.AppDatabase
import com.example.lab4.R
import com.example.lab4.entities.Note
import com.example.lab4.viewModels.AddNoteViewModel
import com.example.lab4.viewModels.PlaceDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AddNoteActivity : AppCompatActivity() {

    private var placeId by Delegates.notNull<Long>()
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var viewModel: AddNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        placeId = intent.getLongExtra("PLACE_ID", 0L)
        notesEditText = findViewById(R.id.notes_edit_text)
        saveButton = findViewById(R.id.save_button)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[AddNoteViewModel::class.java]

        saveButton.setOnClickListener {
            val notes = notesEditText.text.toString()

            if (notes.isBlank()) {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(
                place_id = placeId,
                notes = notes
            )

            // Добавляем заметку через ViewModel
            viewModel.addNote(note)
        }

        // Наблюдение за статусом добавления заметки
        viewModel.noteAdded.observe(this) { noteAdded ->
            if (noteAdded) {
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK) // Устанавливаем результат для вызвавшей активности
                finish()
                viewModel.resetNoteAddedFlag() // Сбрасываем состояние
            }
        }
    }
}

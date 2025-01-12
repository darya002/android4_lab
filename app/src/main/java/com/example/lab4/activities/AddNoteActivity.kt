package com.example.lab4.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.AppDatabase
import com.example.lab4.R
import com.example.lab4.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AddNoteActivity : AppCompatActivity() {

    private var placeId by Delegates.notNull<Long>()
    private lateinit var notesEditText: EditText
    private lateinit var visitedCheckBox: CheckBox
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        placeId = intent.getLongExtra("PLACE_ID", 0L)

        notesEditText = findViewById(R.id.notes_edit_text)
        visitedCheckBox = findViewById(R.id.visited_checkbox)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val notes = notesEditText.text.toString()
            val visited = visitedCheckBox.isChecked

            val note = Note(
                place_id = placeId,
                visited = visited,
                notes = notes
            )

            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(applicationContext).noteDao().insertNote(note)

                runOnUiThread {
                    Toast.makeText(this@AddNoteActivity, "Note added", Toast.LENGTH_SHORT).show()

                    // Переход на MainActivity после добавления заметки
                    val intent = Intent(this@AddNoteActivity, MainActivity::class.java)
                    startActivity(intent)

                    // Завершаем текущую активность
                    finish()
                }
            }
        }
    }
}

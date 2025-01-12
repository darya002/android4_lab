package com.example.lab4.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.adapter.NotesAdapter
import com.example.lab4.entities.Note
import com.example.lab4.entities.Place
import com.example.lab4.viewModels.PlaceDetailsViewModel

class PlaceDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: PlaceDetailsViewModel
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var notesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        viewModel = ViewModelProvider(this)[PlaceDetailsViewModel::class.java]

        val place = intent.getParcelableExtra<Place>("PLACE")
        if (place == null) {
            Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModel.setPlace(place)

        val placeNameDetail: TextView = findViewById(R.id.place_name_detail)
        val placeDescriptionDetail: TextView = findViewById(R.id.place_description_detail)
        val placeType: TextView = findViewById(R.id.place_type)
        val placeLocation: TextView = findViewById(R.id.place_location)
        val editButton: Button = findViewById(R.id.edit_button)
        val deleteButton: Button = findViewById(R.id.delete_button)
        val addNoteButton: Button = findViewById(R.id.add_note_button)
        notesRecyclerView = findViewById(R.id.notes_recycler_view)

        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(mutableListOf()) { note ->
            deleteNote(note)
        }
        notesRecyclerView.adapter = notesAdapter

        viewModel.place.observe(this) { currentPlace ->
            if (currentPlace != null) {
                placeNameDetail.text = currentPlace.title
                placeDescriptionDetail.text = currentPlace.description
                placeLocation.text = currentPlace.location
                placeType.text = currentPlace.type
                loadNotesForPlace(currentPlace.id)
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, AddEditPlaceActivity::class.java)
            intent.putExtra("PLACE", place)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            viewModel.deletePlace(place)
            Toast.makeText(this, "Place deleted", Toast.LENGTH_SHORT).show()
            finish()
        }

        addNoteButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("PLACE_ID", place.id)
            startActivity(intent)
        }
    }

    private fun loadNotesForPlace(placeId: Long) {
        viewModel.getNotesForPlace(placeId).observe(this) { notes ->
            notesAdapter.updateNotes(notes) // Обновляем данные в адаптере
        }
    }

    private fun deleteNote(note: Note) {
        viewModel.deleteNote(note) // Удаляем заметку из базы
        notesAdapter.removeNote(note) // Удаляем из списка адаптера
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
    }
}

package com.example.lab4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.entities.Note

class NotesAdapter(
    private var notes: MutableList<Note>,
    private val onDeleteNote: (Note) -> Unit // Callback для удаления заметки
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteText: TextView = itemView.findViewById(R.id.note_text)
        val deleteButton: Button = itemView.findViewById(R.id.delete_note_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.noteText.text = note.notes

        // Обработчик кнопки удаления
        holder.deleteButton.setOnClickListener {
            onDeleteNote(note) // Вызов callback для удаления
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    // Метод для обновления списка заметок
    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    // Метод для удаления заметки
    fun removeNote(note: Note) {
        val position = notes.indexOf(note)
        if (position != -1) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}

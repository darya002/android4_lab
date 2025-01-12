package com.example.lab4.contentProvider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.room.Room
import com.example.lab4.AppDatabase
import com.example.lab4.entities.Note
import com.example.lab4.entities.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AppContentProvider : ContentProvider() {

    private lateinit var database: AppDatabase

    override fun onCreate(): Boolean {
        // Инициализация базы данных
        context?.let {
            database = Room.databaseBuilder(it, AppDatabase::class.java, "app_db").build()
        }
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return runBlocking {
            when (URI_MATCHER.match(uri)) {
                PLACES -> {
                    val place = Place(
                        title = values?.getAsString("title") ?: "",
                        description = values?.getAsString("description") ?: "",
                        location = values?.getAsString("location") ?: "",
                        type = values?.getAsString("type") ?: ""
                    )
                    val id = withContext(Dispatchers.IO) {
                        database.placeDao().insertPlace(place)
                    }
                    return@runBlocking Uri.withAppendedPath(uri, id.toString()) // Возвращаем Uri с ID
                }
                NOTES -> {
                    val note = Note(
                        place_id = values?.getAsLong("place_id") ?: 0L,
                        notes = values?.getAsString("notes") ?: ""
                    )
                    val id = withContext(Dispatchers.IO) {
                        database.noteDao().insertNote(note)
                    }
                    return@runBlocking Uri.withAppendedPath(uri, id.toString()) // Возвращаем Uri с ID
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }


    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("id", "title", "description", "location", "type"))
        runBlocking {
            when (URI_MATCHER.match(uri)) {
                PLACES -> {
                    // Используем collect для Flow
                    val places = withContext(Dispatchers.IO) {
                        database.placeDao().getAllPlaces() // Это Flow
                    }
                    places.collect { placesList ->
                        placesList.forEach { place ->
                            cursor.addRow(arrayOf(place.id, place.title, place.description, place.location, place.type))
                        }
                    }
                }
                NOTES -> {
                    val notes = withContext(Dispatchers.IO) {
                        database.noteDao().getAllNotes() // Это Flow
                    }
                    notes.collect { notesList ->
                        notesList.forEach { note ->
                            cursor.addRow(arrayOf(note.id, note.notes))
                        }
                    }
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        return cursor
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var rowsUpdated = 0
        // Запуск корутины для выполнения асинхронной операции в фоновом потоке
        CoroutineScope(Dispatchers.IO).launch {
            when (URI_MATCHER.match(uri)) {
                PLACES -> {
                    val placeId = selectionArgs?.get(0)?.toLong() ?: 0L
                    val place = Place(
                        id = placeId,
                        title = values?.getAsString("title") ?: "",
                        description = values?.getAsString("description") ?: "",
                        location = values?.getAsString("location") ?: "",
                        type = values?.getAsString("type") ?: ""
                    )
                    // Обновляем место в базе данных
                    database.placeDao().updatePlace(place)
                    rowsUpdated = 1
                }
                NOTES -> {
                    val noteId = selectionArgs?.get(0)?.toLong() ?: 0L
                    val note = Note(
                        place_id = values?.getAsLong("place_id") ?: 0L,
                        notes = values?.getAsString("notes") ?: ""
                    )
                    // Обновляем заметку в базе данных
                    database.noteDao().updateNote(note)
                    rowsUpdated = 1
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        return rowsUpdated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var rowsDeleted = 0
        CoroutineScope(Dispatchers.IO).launch {
            when (URI_MATCHER.match(uri)) {
                PLACES -> {
                    val placeId = selectionArgs?.get(0)?.toLong() ?: 0L
                    val place = Place(id = placeId, title = "", description = "", location = "", type = "")
                    database.placeDao().deletePlace(place)
                    rowsDeleted = 1 // Возвращаем количество удаленных строк
                }
                NOTES -> {
                    val noteId = selectionArgs?.get(0)?.toLong() ?: 0L
                    val placeId = selectionArgs?.get(1)?.toLong() ?: 0L // Получаем place_id из selectionArgs

                    // Создаем объект Note с переданным place_id
                    val note = Note(id = noteId, place_id = placeId, notes = "")
                    database.noteDao().deleteNote(note)
                    rowsDeleted = 1 // Возвращаем количество удаленных строк
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        return rowsDeleted
    }


    override fun getType(uri: Uri): String? {
        return when (URI_MATCHER.match(uri)) {
            PLACES -> "vnd.android.cursor.dir/vnd.com.example.app.places"
            NOTES -> "vnd.android.cursor.dir/vnd.com.example.app.notes"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    companion object {
        private const val AUTHORITY = "com.example.app.provider"
        private const val PLACES = 1
        private const val NOTES = 2
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            URI_MATCHER.addURI(AUTHORITY, "places", PLACES)
            URI_MATCHER.addURI(AUTHORITY, "notes", NOTES)
        }

        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
    }
}

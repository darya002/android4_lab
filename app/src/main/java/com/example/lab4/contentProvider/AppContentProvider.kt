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
import kotlinx.coroutines.launch

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
        CoroutineScope(Dispatchers.IO).launch {
            when (URI_MATCHER.match(uri)) {
                PLACES -> {
                    val place = Place(
                        title = values?.getAsString("title") ?: "",
                        description = values?.getAsString("description") ?: "",
                        location = values?.getAsString("location") ?: "",
                        type = values?.getAsString("type") ?: ""
                    )
                    // Асинхронно вставляем данные в базу
                    database.placeDao().insertPlace(place)
                }
                NOTES -> {
                    val note = Note(
                        place_id = values?.getAsLong("place_id") ?: 0L,  // Добавляем place_id
                        visited = values?.getAsBoolean("visited") ?: false,
                        notes = values?.getAsString("notes") ?: ""
                    )
                    // Асинхронно вставляем данные в базу
                    database.noteDao().insertNote(note)
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        return Uri.withAppendedPath(uri, "someId") // Здесь возвращаем URI с каким-либо ID
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        // Создаем MatrixCursor с колонками
        val cursor = MatrixCursor(arrayOf("id", "title", "description", "location", "type"))

        // Запускаем корутину для работы с базой данных
        CoroutineScope(Dispatchers.IO).launch {
            when (URI_MATCHER.match(uri)) {
                PLACES -> {
                    // Получаем список мест из базы данных (через Flow)
                    database.placeDao().getAllPlaces().collect { places ->
                        // Используем collect для обработки элементов
                        places.forEach { place ->
                            // Добавляем строку в курсор
                            cursor.addRow(arrayOf(place.id, place.title, place.description, place.location, place.type))
                        }
                    }
                }
                NOTES -> {
                    // Получаем список заметок из базы данных (через Flow)
                    database.noteDao().getAllNotes().collect { notes ->
                        // Используем collect для обработки элементов
                        notes.forEach { note ->
                            // Добавляем строку в курсор
                            cursor.addRow(arrayOf(note.id, note.visited, note.notes))
                        }
                    }
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        }

        // Возвращаем курсор
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
                    rowsUpdated = 1 // Возвращаем количество обновленных строк
                }
                NOTES -> {
                    val noteId = selectionArgs?.get(0)?.toLong() ?: 0L
                    val note = Note(
                        place_id = values?.getAsLong("place_id") ?: 0L,  // Убедитесь, что place_id передается
                        visited = values?.getAsBoolean("visited") ?: false,
                        notes = values?.getAsString("notes") ?: ""
                    )
                    // Обновляем заметку в базе данных
                    database.noteDao().updateNote(note)
                    rowsUpdated = 1 // Возвращаем количество обновленных строк
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
                    val note = Note(id = noteId, place_id = placeId, visited = false, notes = "")
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

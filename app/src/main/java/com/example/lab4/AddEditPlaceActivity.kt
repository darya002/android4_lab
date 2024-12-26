package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.entities.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditPlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_place)

        // Получаем ссылки на поля ввода
        val placeName: EditText = findViewById(R.id.place_name)
        val placeDescription: EditText = findViewById(R.id.place_description)
        val placeLocation: EditText = findViewById(R.id.place_location)
        val placeType: EditText = findViewById(R.id.place_type)
        val saveButton: Button = findViewById(R.id.save_button)

        // Получаем объект Place из Intent (используем getParcelableExtra)
        val place = intent.getParcelableExtra<Place>("PLACE")

        // Логируем, чтобы проверить, передаются ли данные
        Log.d("AddEditPlaceActivity", "Place received: $place")

        if (place != null) {
            // Заполняем поля данными из объекта Place
            placeName.setText(place.title)
            placeDescription.setText(place.description)
            placeLocation.setText(place.location)
            placeType.setText(place.type)
        } else {
            Log.e("AddEditPlaceActivity", "Place object is null!")
        }

        saveButton.setOnClickListener {
            // Получаем данные из полей
            val name = placeName.text.toString()
            val description = placeDescription.text.toString()
            val location = placeLocation.text.toString()
            val type = placeType.text.toString()

            // Создаем новый объект Place для сохранения
            val updatedPlace = Place(
                id = place?.id ?: 0,
                title = name,
                description = description,
                location = location,
                type = type
            )

            val placeDao = AppDatabase.getDatabase(this).placeDao()
            CoroutineScope(Dispatchers.IO).launch {
                if (place != null) {
                    placeDao.updatePlace(updatedPlace) // Обновляем место
                } else {
                    placeDao.insertPlace(updatedPlace) // Добавляем новое место
                }

                // Переход на MainActivity после сохранения
                val intent = Intent(this@AddEditPlaceActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // Закрытие текущей активности
            }
        }
    }
}

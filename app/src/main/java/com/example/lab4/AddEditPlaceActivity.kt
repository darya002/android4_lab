package com.example.lab4

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.entities.Place
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditPlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_place)

        val placeName: TextInputEditText = findViewById(R.id.place_name)
        val placeDescription: TextInputEditText = findViewById(R.id.place_description)
        val placeLocation: TextInputEditText = findViewById(R.id.place_location)
        val placeType: TextInputEditText = findViewById(R.id.place_type)
        val saveButton: Button = findViewById(R.id.save_button)

        val place = intent.getSerializableExtra("PLACE") as? Place

        if (place != null) {
            placeName.setText(place.title)
            placeDescription.setText(place.description)
            placeLocation.setText(place.location)
            placeType.setText(place.type)
        }

        saveButton.setOnClickListener {
            val name = placeName.text.toString()
            val description = placeDescription.text.toString()
            val location = placeLocation.text.toString()
            val type = placeType.text.toString()

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
                    placeDao.updatePlace(updatedPlace) // Обновление места
                } else {
                    placeDao.insertPlace(updatedPlace) // Добавление нового места
                }
                finish()
            }
        }
    }
}

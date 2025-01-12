package com.example.lab4.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.R
import com.example.lab4.entities.Place
import com.example.lab4.viewModels.AddEditPlaceViewModel

class AddEditPlaceActivity : AppCompatActivity() {

    private lateinit var viewModel: AddEditPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_place)

        viewModel = ViewModelProvider(this)[AddEditPlaceViewModel::class.java]

        val placeName: EditText = findViewById(R.id.place_name)
        val placeDescription: EditText = findViewById(R.id.place_description)
        val placeLocation: EditText = findViewById(R.id.place_location)
        val placeType: EditText = findViewById(R.id.place_type)
        val visitedCheckBox: CheckBox = findViewById(R.id.visited_checkbox) // Галочка для посещения
        val saveButton: Button = findViewById(R.id.save_button)

        val place = intent.getParcelableExtra<Place>("PLACE")
        viewModel.setPlace(place)

        viewModel.place.observe(this) { currentPlace ->
            if (currentPlace != null) {
                placeName.setText(currentPlace.title)
                placeDescription.setText(currentPlace.description)
                placeLocation.setText(currentPlace.location)
                placeType.setText(currentPlace.type)
                visitedCheckBox.isChecked = currentPlace.visited // Отображаем статус посещения
            }
        }

        saveButton.setOnClickListener {
            val updatedPlace = Place(
                id = place?.id ?: 0,
                title = placeName.text.toString(),
                description = placeDescription.text.toString(),
                location = placeLocation.text.toString(),
                type = placeType.text.toString(),
                visited = visitedCheckBox.isChecked
            )
            viewModel.savePlace(updatedPlace)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("PLACE", updatedPlace)
            startActivity(intent)
            finish()
        }
    }
}

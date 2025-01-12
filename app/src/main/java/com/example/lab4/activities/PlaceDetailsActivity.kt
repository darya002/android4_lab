package com.example.lab4.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.R
import com.example.lab4.entities.Place
import com.example.lab4.viewModels.PlaceDetailsViewModel

class PlaceDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: PlaceDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[PlaceDetailsViewModel::class.java]

        // Получаем объект Place из Intent
        val place = intent.getParcelableExtra<Place>("PLACE")
        if (place == null) {
            Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModel.setPlace(place)

        val placeNameDetail: TextView = findViewById(R.id.place_name_detail)
        val placeDescriptionDetail: TextView = findViewById(R.id.place_description_detail)
        val editButton: Button = findViewById(R.id.edit_button)
        val deleteButton: Button = findViewById(R.id.delete_button)

        // Подписка на изменения в данных места
        viewModel.place.observe(this) { currentPlace ->
            if (currentPlace != null) {
                placeNameDetail.text = currentPlace.title
                placeDescriptionDetail.text = currentPlace.description
            }
        }

        // Редактирование
        editButton.setOnClickListener {
            val intent = Intent(this, AddEditPlaceActivity::class.java)
            intent.putExtra("PLACE", place)
            startActivity(intent)
        }

        // Удаление
        deleteButton.setOnClickListener {
            viewModel.deletePlace()
            Toast.makeText(this, "Place deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

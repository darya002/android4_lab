package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab4.entities.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)

        // Получаем объект Place из Intent, проверяя, что он не null
        val place = intent.getSerializableExtra("PLACE") as? Place
        if (place == null) {
            Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show()
            finish() // Завершаем активность, если место не найдено
            return
        }

        val placeNameDetail: TextView = findViewById(R.id.place_name_detail)
        val placeDescriptionDetail: TextView = findViewById(R.id.place_description_detail)
        val editButton: Button = findViewById(R.id.edit_button)
        val deleteButton: Button = findViewById(R.id.delete_button)

        placeNameDetail.text = place.title
        placeDescriptionDetail.text = place.description

        // Открытие экрана редактирования
        editButton.setOnClickListener {
            val intent = Intent(this, AddEditPlaceActivity::class.java)
            intent.putExtra("PLACE", place) // Передаем место в AddEditPlaceActivity
            startActivity(intent)
        }

        // Удаление места
        deleteButton.setOnClickListener {
            val placeDao = AppDatabase.getDatabase(this).placeDao()
            CoroutineScope(Dispatchers.IO).launch {
                placeDao.deletePlace(place)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PlaceDetailsActivity, "Place deleted", Toast.LENGTH_SHORT).show()
                    finish() // Закрываем активность после удаления
                }
            }
        }
    }
}

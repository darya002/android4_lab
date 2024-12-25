package com.example.lab4

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.lab4.adapter.PlaceAdapter
import com.example.lab4.entities.Place
import com.example.lab4.ui.theme.Lab4Theme

class MainActivity : ComponentActivity() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация ViewModel с Application
        appViewModel = ViewModelProvider(this, AppViewModelFactory(application)).get(AppViewModel::class.java)

        // Настроим RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPlaces)
        placeAdapter = PlaceAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = placeAdapter

        // Наблюдаем за данными
        appViewModel.allPlaces.observe(this, Observer { places ->
            // Обновляем данные в адаптере
            placeAdapter = PlaceAdapter(places)
            recyclerView.adapter = placeAdapter
        })

        // Обработчик для кнопки "Добавить место"
        findViewById<Button>(R.id.buttonAddPlace).setOnClickListener {
            // Открыть форму для добавления нового места
            val newPlace = Place(title = "New Place", description = "New Description", location = "Location", type = "Museum")
            appViewModel.addPlace(newPlace)
        }
    }
}

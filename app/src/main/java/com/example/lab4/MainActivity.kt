package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.entities.Place
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.adapter.PlaceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Подписываемся на данные из ViewModel
        appViewModel.allPlaces.collectInLifecycle(this) { places ->
            placeAdapter = PlaceAdapter(
                places,
                onItemClick = { place ->
                    val intent = Intent(this@MainActivity, PlaceDetailsActivity::class.java)
                    intent.putExtra("PLACE", place)
                    startActivity(intent)
                },
                onVisitedChange = { place, isChecked ->
                    appViewModel.updateVisited(place.id, isChecked)
                }
            )
            recyclerView.adapter = placeAdapter
        }

        // Кнопка добавления нового места
        findViewById<Button>(R.id.add_button).setOnClickListener {
            val intent = Intent(this, AddEditPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}

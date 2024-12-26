package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.lab4.entities.Place
import com.example.lab4.ui.theme.Lab4Theme
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
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
    private var places: List<Place> = emptyList() // Изначально пустой список

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val placeDao = AppDatabase.getDatabase(this).placeDao()

        // Используем корутины для асинхронного получения данных
        CoroutineScope(Dispatchers.IO).launch {
            places = placeDao.getAllPlaces().first() // Получаем список мест из потока

            // Обновляем UI на главном потоке
            withContext(Dispatchers.Main) {
                // Настройка адаптера для RecyclerView
                placeAdapter = PlaceAdapter(places) { place ->
                    // Обработка клика по элементу
                    val intent = Intent(this@MainActivity, PlaceDetailsActivity::class.java)
                    // Убедитесь, что place не null
                    if (place != null) {
                        intent.putExtra("PLACE", place)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Place not found!", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerView.adapter = placeAdapter
            }
        }

        // Кнопка добавления нового места
        findViewById<Button>(R.id.add_button).setOnClickListener {
            val intent = Intent(this, AddEditPlaceActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем список мест после возвращения из другой активности
        val placeDao = AppDatabase.getDatabase(this).placeDao()
        CoroutineScope(Dispatchers.IO).launch {
            places = placeDao.getAllPlaces().first() // Получаем актуальный список мест
            withContext(Dispatchers.Main) {
                placeAdapter = PlaceAdapter(places) { place ->
                    // Обработка клика по элементу
                    val intent = Intent(this@MainActivity, PlaceDetailsActivity::class.java)
                    // Убедитесь, что place не null
                    if (place != null) {
                        intent.putExtra("PLACE", place)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Place not found!", Toast.LENGTH_SHORT).show()
                    }
                }
                recyclerView.adapter = placeAdapter
            }
        }
    }
}

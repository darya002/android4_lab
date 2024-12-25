package com.example.lab4

import android.os.Bundle
import android.widget.Button
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

class MainActivity : ComponentActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализация ViewModel
        appViewModel = ViewModelProvider(this, AppViewModelFactory(application)).get(AppViewModel::class.java)

        // Установка контента с использованием Compose
        setContent {
            Lab4Theme {
                // Передаем appViewModel в Composable функции
                MainScreen(appViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(appViewModel: AppViewModel) {
    // Получаем список мест через collectAsState
    val allPlaces = appViewModel.allPlaces.collectAsState(initial = emptyList()).value

    // Показываем список мест
    PlacesList(places = allPlaces)

    // Кнопка для добавления нового места
    AddPlaceButton(appViewModel)
}

@Composable
fun PlacesList(places: List<Place>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(places) { place ->
            PlaceItem(place = place)
        }
    }
}

@Composable
fun PlaceItem(place: Place) {
    Text(text = place.title)
}

@Composable
fun AddPlaceButton(appViewModel: AppViewModel) {
    Button(onClick = {
        // Добавляем новое место
        val newPlace = Place(title = "New Place", description = "New Description", location = "Location", type = "Museum")
        appViewModel.addPlace(newPlace)
    }) {
        Text("Add New Place")
    }
}

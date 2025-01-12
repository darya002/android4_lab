package com.example.lab4.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.adapter.PlaceAdapter
import com.example.lab4.collectInLifecycle
import com.example.lab4.viewModels.AppViewModel

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

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

        findViewById<Button>(R.id.add_button).setOnClickListener {
            val intent = Intent(this, AddEditPlaceActivity::class.java)
            startActivity(intent)
        }
    }
}

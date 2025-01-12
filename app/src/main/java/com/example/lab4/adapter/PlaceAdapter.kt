package com.example.lab4.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.R
import com.example.lab4.entities.Place

class PlaceAdapter(
    private val places: List<Place>,
    private val onItemClick: (Place) -> Unit,
    private val onVisitedChange: (Place, Boolean) -> Unit // Новый параметр для обработки изменений чекбокса
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int = places.size

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeName: TextView = itemView.findViewById(R.id.place_name)
        private val placeDescription: TextView = itemView.findViewById(R.id.place_description)
        private val placeVisitedCheckbox: CheckBox = itemView.findViewById(R.id.place_visited_checkbox)

        fun bind(place: Place) {
            placeName.text = place.title
            placeDescription.text = place.description
            placeVisitedCheckbox.isChecked = place.visited

            itemView.setOnClickListener {
                onItemClick(place)
            }

            placeVisitedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onVisitedChange(place, isChecked)
            }
        }
    }
}

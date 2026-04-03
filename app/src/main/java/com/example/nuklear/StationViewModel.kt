package com.example.nuklear

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class StationViewModel : ViewModel() {
    // Ensure the initial list is populated with your sample data
    private val _stations = mutableStateListOf<Station>().apply {
        addAll(sampleStations)
    }

    val stations: List<Station> get() = _stations

    val favoriteStations: List<Station>
        get() = _stations.filter { it.isFavorite }

    // Change parameter type to Int to match your Station ID
    fun toggleFavorite(stationId: Int) {
        val index = _stations.indexOfFirst { it.id == stationId }
        if (index != -1) {
            val station = _stations[index]
            _stations[index] = station.copy(isFavorite = !station.isFavorite)
        }
    }
}
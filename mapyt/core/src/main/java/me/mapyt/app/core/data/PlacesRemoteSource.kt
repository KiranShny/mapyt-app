package me.mapyt.app.core.data

import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.entities.PlaceNearbySearch

interface PlacesRemoteSource {
    suspend fun searchNearby(location: String, radius: Int): List<Place>
}
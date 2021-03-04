package me.mapyt.app.core.data

import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.domain.entities.PlaceNearbySearch

interface PlacesRemoteSource {
    suspend fun searchNearby(keyword: String?, location: String, radius: Int): List<Place>
    suspend fun getDetails(id: String): PlaceDetails
    fun getPhotoPath(reference: String, maxHeight: Int): String
}
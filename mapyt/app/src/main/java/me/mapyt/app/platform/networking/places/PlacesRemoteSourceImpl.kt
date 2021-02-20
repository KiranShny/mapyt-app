package me.mapyt.app.platform.networking.places

import me.mapyt.app.core.data.PlacesRemoteSource
import me.mapyt.app.core.domain.entities.Place

class PlacesRemoteSourceImpl(private val service: PlacesService) : PlacesRemoteSource {

    override suspend fun searchNearby(location: String, radius: Int): List<Place> {
        val results = service.searchNearby(location, radius)
        return results.toPlaces()
    }
}
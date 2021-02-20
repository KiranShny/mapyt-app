package me.mapyt.app.platform.networking.places

import me.mapyt.app.core.data.PlacesRemoteSource
import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.shared.InvalidResponseException

class PlacesRemoteSourceImpl(private val service: PlacesService) : PlacesRemoteSource {

    override suspend fun searchNearby(keyword: String, location: String, radius: Int): List<Place> {
        val response = service.searchNearby(keyword, location, radius)
        //TODO: mover a interceptor
        if(isInvalidStatus(response.status)) throw InvalidResponseException(response.status)
        return response.toPlaces()
    }
}
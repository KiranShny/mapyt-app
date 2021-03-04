package me.mapyt.app.core.data

import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.entities.PlaceDetails

class PlacesRepository(private val remoteSource: PlacesRemoteSource) {

    companion object {
        const val KEYWORD_SEPARATOR = "|"
    }

    suspend fun searchNearby(keywords: List<String>?, location: String, radius: Int): List<Place> {
        //TODO: implementar local data source
        return remoteSource.searchNearby(joinKeywords(keywords), location, radius)
    }

    suspend fun getDetails(placeId: String): PlaceDetails {
        //TODO: implementar local data source
        return remoteSource.getDetails(placeId)
    }

    fun getPhotoPath(reference: String, maxHeight: Int) =
        remoteSource.getPhotoPath(reference, maxHeight)

    private fun joinKeywords(keywords: List<String>?) = keywords?.joinToString(KEYWORD_SEPARATOR)
}
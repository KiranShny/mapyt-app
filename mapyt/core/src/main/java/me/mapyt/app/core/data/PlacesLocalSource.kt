package me.mapyt.app.core.data

import me.mapyt.app.core.domain.entities.PlaceDetails

interface PlacesLocalSource {
    suspend fun getAll(): List<PlaceDetails>
    suspend fun get(code: String): PlaceDetails
    suspend fun insert(entity: PlaceDetails)
    suspend fun delete(entity: PlaceDetails)
    suspend fun deleteAll()
}
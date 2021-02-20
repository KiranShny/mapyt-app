package me.mapyt.app.platform.networking.places

import me.mapyt.app.platform.networking.places.ApiConstants.PARAM_LOCATION
import me.mapyt.app.platform.networking.places.ApiConstants.PARAM_RADIUS
import me.mapyt.app.platform.networking.places.ApiConstants.RESOURCE_NEARBY
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {

    @GET(RESOURCE_NEARBY)
    suspend fun searchNearby(
        @Query(PARAM_LOCATION) location: String,
        @Query(PARAM_RADIUS) radius: Int
    ): NearbySearchResponse
}
package me.mapyt.app.platform.networking.places

import com.google.gson.annotations.SerializedName

data class NearbySearchResponse(
    @SerializedName("results") val results: List<PlaceResponse>,
    @SerializedName("status") val status: String,
)

data class PlaceResponse(
    @SerializedName("geometry") val geometry: GeometryResponse,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("name") val name: String,
    @SerializedName("vicinity") val vicinity: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("user_ratings_total") val userRatingsTotal: Double?,
    @SerializedName("photos") val photos: List<PhotoResponse>?,
)

data class GeometryResponse(
    @SerializedName("location") val location: LocationResponse,
)

data class LocationResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)

data class PhotoResponse(
    @SerializedName("photo_reference") val reference: String,
    @SerializedName("height") val height: Double,
    @SerializedName("width") val width: Double,
)
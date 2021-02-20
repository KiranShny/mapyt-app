package me.mapyt.app.core.domain.entities

data class PlaceNearbySearch(
    val results: List<Place>,
)

data class Place(
    val geometry: PlaceGeometry,
    val placeId: String,
    val name: String,
    val address: String?,
    val rating: Double?,
    val userRatingsTotal: Double?,
    val photos: List<PlacePhoto>?,
)

data class PlaceGeometry(
    val location: PlaceLocation,
)

data class PlaceLocation(
    val lat: Double,
    val lng: Double,
)

data class PlacePhoto(
    val reference: String,
    val height: Double,
    val width: Double,
)
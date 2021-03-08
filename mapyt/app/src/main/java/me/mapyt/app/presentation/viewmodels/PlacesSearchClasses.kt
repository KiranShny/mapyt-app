package me.mapyt.app.presentation.viewmodels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.entities.PlaceDetails

data class UserPosition(
    val code: String,
    val lat: Double,
    val lng: Double,
    val zoomLevel: Float,
    val title: String? = null,
)

@Parcelize
data class MapPlace(
    val code: String,
    val lat: Double,
    val lng: Double,
    val name: String? = null,
    val address: String? = null,
    val rating: Double? = null,
    val photosRefs: List<String>? = null,
) : Parcelable

fun PlaceDetails.toMaster() = MapPlace(code = placeId,
    lat = geometry.location.lat,
    lng = geometry.location.lng,
    name = name,
    address = address,
    rating = rating,
    photosRefs = photos?.map { it.reference })
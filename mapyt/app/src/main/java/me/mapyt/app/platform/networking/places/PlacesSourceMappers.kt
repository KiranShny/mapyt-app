package me.mapyt.app.platform.networking.places

import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.entities.PlaceGeometry
import me.mapyt.app.core.domain.entities.PlaceLocation
import me.mapyt.app.core.domain.entities.PlacePhoto

fun NearbySearchResponse.toPlaces(): List<Place> = results.map {
    it.run {
        Place(
            geometry = geometry.toEntity(),
            placeId = placeId,
            name = name,
            address = vicinity,
            rating = rating,
            userRatingsTotal = userRatingsTotal,
            photos = photos?.map { photo -> photo.toEntity() })
    }
}

fun GeometryResponse.toEntity(): PlaceGeometry = PlaceGeometry(location.toEntity())

fun LocationResponse.toEntity(): PlaceLocation = PlaceLocation(lat = lat, lng = lng)

fun PhotoResponse.toEntity(): PlacePhoto = PlacePhoto(reference, width = width, height = height)
package me.mapyt.app.platform.database.entities

import me.mapyt.app.core.domain.entities.*

fun SavedPlaceEntity.toDomainPlace() = Place(
    geometry = toGeometry(lat, lng),
    placeId = placeId,
    name = name,
    address = address,
    rating = rating,
    userRatingsTotal = userRatingsTotal,
    photos = toPhotos(photosRefs)
)

fun SavedPlaceEntity.toDomainDetails() = PlaceDetails(
    geometry = toGeometry(lat, lng),
    placeId = placeId,
    name = name,
    address = address,
    rating = rating,
    userRatingsTotal = userRatingsTotal,
    photos = toPhotos(photosRefs),
    reviews = emptyList() //TODO: implementar
)

fun PlaceDetails.toSavedPlaceEntity() = SavedPlaceEntity(
    placeId = placeId,
    name = name,
    lat = geometry.location.lat,
    lng = geometry.location.lng,
    address = address ?: "",
    rating = rating ?: 0.0,
    userRatingsTotal = userRatingsTotal ?: 0.0,
    photosRefs = toPhotosRefs(photos)
)

private fun toGeometry(lat: Double, lng: Double) =
    PlaceGeometry(location = PlaceLocation(lat, lng))

private fun toPhotos(photosRefs: List<String>) =
    photosRefs.map { PlacePhoto(reference = it, height = 0.0, width = 0.0) }

private fun toPhotosRefs(photos: List<PlacePhoto>?) =
    photos?.map { it.reference } ?: emptyList()
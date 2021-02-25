package me.mapyt.app.platform.networking.places

import me.mapyt.app.core.domain.entities.*

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

fun PlaceDetailsResponse.toPlaceDetails(): PlaceDetails = with(result) {
    PlaceDetails(
        geometry = geometry.toEntity(),
        placeId = placeId,
        name = name,
        address = vicinity,
        rating = rating,
        userRatingsTotal = userRatingsTotal,
        photos = photos?.map { photo -> photo.toEntity() },
        reviews = reviews?.map { review -> review.toEntity() } ?: emptyList(),
    )
}

fun GeometryResponse.toEntity(): PlaceGeometry = PlaceGeometry(location.toEntity())

fun LocationResponse.toEntity(): PlaceLocation = PlaceLocation(lat = lat, lng = lng)

fun PhotoResponse.toEntity(): PlacePhoto = PlacePhoto(reference, width = width, height = height)

fun ReviewResponse.toEntity(): PlaceReview =
    PlaceReview(authorName, authorPhotoUrl, rating, content, timeDescription, time)
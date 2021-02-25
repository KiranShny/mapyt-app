package me.mapyt.app.platform.networking.places

import com.google.gson.annotations.SerializedName

data class PlaceDetailsResponse (
    @SerializedName("result") val result: PlaceDetailsBodyResponse,
    @SerializedName("status") val status: String,
)

data class PlaceDetailsBodyResponse(
    @SerializedName("geometry") val geometry: GeometryResponse,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("name") val name: String,
    @SerializedName("reference") val reference: String?,
    @SerializedName("vicinity") val vicinity: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("user_ratings_total") val userRatingsTotal: Double?,
    @SerializedName("photos") val photos: List<PhotoResponse>?,
    @SerializedName("reviews") val reviews: List<ReviewResponse>?,
)

data class ReviewResponse(
    @SerializedName("author_name") val authorName: String,
    @SerializedName("profile_photo_url") val authorPhotoUrl: String?,
    @SerializedName("rating") val rating: Double,
    @SerializedName("text") val content: String,
    @SerializedName("relative_time_description") val timeDescription: String,
    @SerializedName("time") val time: Int,
)
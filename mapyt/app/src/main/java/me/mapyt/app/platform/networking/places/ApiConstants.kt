package me.mapyt.app.platform.networking.places

object ApiConstants {
    const val BASE_API_URL = "https://maps.googleapis.com/maps/api/place/"

    const val RESOURCE_NEARBY = "nearbysearch/json"
    const val RESOURCE_DETAILS = "details/json"

    const val PARAM_KEY = "key"
    const val PARAM_KEYWORD = "keyword"
    const val PARAM_LOCATION = "location"
    const val PARAM_RADIUS = "radius"
}

object ApiStatus {
    const val STATUS_OK = "OK"
}

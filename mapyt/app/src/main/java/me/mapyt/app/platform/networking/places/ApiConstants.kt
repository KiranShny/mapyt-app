package me.mapyt.app.platform.networking.places

import me.mapyt.app.BuildConfig

object ApiConstants {
    //TODO: volver ApiClient una class y pasar key como param.
    internal const val API_KEY = BuildConfig.GPLACES_KEY
    const val BASE_API_URL = "https://maps.googleapis.com/maps/api/place/"

    const val RESOURCE_NEARBY = "nearbysearch/json"
    const val RESOURCE_DETAILS = "details/json"
    const val RESOURCE_PHOTO_FILE = "photo"

    const val PARAM_KEY = "key"
    const val PARAM_KEYWORD = "keyword"
    const val PARAM_LOCATION = "location"
    const val PARAM_RADIUS = "radius"
    const val PARAM_PHOTO_REF = "photoreference"
    const val PARAM_PHOTO_MAX_HEIGHT = "maxheight"

    const val PARAM_PLACE_ID = "place_id"
}

object ApiStatus {
    const val STATUS_OK = "OK"
}

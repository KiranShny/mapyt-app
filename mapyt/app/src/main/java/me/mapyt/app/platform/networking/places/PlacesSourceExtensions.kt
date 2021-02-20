package me.mapyt.app.platform.networking.places

import me.mapyt.app.core.data.PlacesRemoteSource

fun PlacesRemoteSource.isInvalidStatus(status: String) = status != ApiStatus.STATUS_OK
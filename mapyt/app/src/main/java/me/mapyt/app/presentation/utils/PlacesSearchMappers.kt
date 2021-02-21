package me.mapyt.app.presentation.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.presentation.viewmodels.MapPlace

internal fun Marker.toPlace() =
    MapPlace(tag.toString(), position.latitude, position.longitude, title)

internal fun MapPlace.toMarkerOpts() =
    MarkerOptions()
        .position(LatLng(lat, lng))
        .title(name)

internal fun Place.toMapPlace() =
    MapPlace(code = placeId, lat = geometry.location.lat, lng = geometry.location.lng, name = name)
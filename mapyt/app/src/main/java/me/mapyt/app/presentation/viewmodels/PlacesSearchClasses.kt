package me.mapyt.app.presentation.viewmodels

import me.mapyt.app.core.domain.entities.Place

data class UserPosition(
    val code: String,
    val lat: Double,
    val lng: Double,
    val zoomLevel: Float,
    val title: String? = null,
)

data class MapPlace(val code: String, val lat: Double, val lng: Double, val name: String? = null)
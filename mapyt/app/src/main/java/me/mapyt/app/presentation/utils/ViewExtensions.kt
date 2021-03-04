package me.mapyt.app.presentation.utils

import android.view.View
import com.google.android.material.progressindicator.CircularProgressIndicator

fun View.shouldBeVisible(isVisible: Boolean, forceToGone: Boolean = false) {
    if(isVisible) {
        visibility = View.VISIBLE
        return
    }
    if(!isVisible && !forceToGone) {
        visibility = View.INVISIBLE
        return
    }
    visibility = View.GONE
}
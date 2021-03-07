package me.mapyt.app.presentation.binding

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("isOn", "srcOn", "srcOff")
fun toggleSrcCompat(
    view: FloatingActionButton,
    isOn: Boolean,
    srcOn: Drawable,
    srcOff: Drawable,
) {
    view.setImageDrawable(if (isOn) {
        srcOn
    } else {
        srcOff
    })
}
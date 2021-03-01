package me.mapyt.app.presentation.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import me.mapyt.app.R
import kotlin.math.roundToInt

private const val CODE_STAR_FILLED = 0x2605
private const val CODE_STAR_EMPTY  = 0x2606
private const val RATING_MAX_VALUE = 5

@BindingAdapter("rating")
fun setRating(view: TextView, rating: Double?) {
    rating?.let {
        val value = it.roundToInt()
        var newValue = ""
        if(value <= 0 || value > 5) {
            newValue = unicodeToString(CODE_STAR_EMPTY).repeat(RATING_MAX_VALUE)
        } else if (value == 5) {
            newValue = unicodeToString(CODE_STAR_FILLED).repeat(value)
        } else {
            newValue = unicodeToString(CODE_STAR_FILLED).repeat(value) + unicodeToString(
                CODE_STAR_EMPTY).repeat(RATING_MAX_VALUE - value)
        }
        view.text = newValue
    }
}

private fun unicodeToString(codePoint: Int): String {
    return String(Character.toChars(codePoint))
}
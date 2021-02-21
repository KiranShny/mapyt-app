package me.mapyt.app.presentation.binding

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import me.mapyt.app.R

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, imagePath: String?) {
    if (!imagePath.isNullOrEmpty()) {
        Picasso
            .get()
            .load(imagePath)
            .placeholder(R.color.colorForPlaceholder)
            .into(view);
    } else {
        view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorForMedia));
    }
}
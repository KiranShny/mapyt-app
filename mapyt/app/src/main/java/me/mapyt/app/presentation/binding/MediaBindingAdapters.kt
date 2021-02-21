package me.mapyt.app.presentation.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import me.mapyt.app.R

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, imagePath: String?) {
    if(!imagePath.isNullOrEmpty())
        Picasso
            .get()
            .load(imagePath).placeholder(R.mipmap.ic_launcher)
            .into(view);
}
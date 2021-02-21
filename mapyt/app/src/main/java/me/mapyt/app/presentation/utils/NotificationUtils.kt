package me.mapyt.app.presentation.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import me.mapyt.app.R

object MessageBar {

    fun showInfo(context: Context?, containerView: View, message: String) {
        if(context == null) return
        return show(context, containerView, message, R.color.colorOnInfo)
    }

    fun showError(context: Context?, containerView: View, message: String?) {
        if(context == null) return
        val text = message ?: context.getString(R.string.unknown_error)
        return show(context, containerView, text, R.color.colorOnError)
    }

    private fun show(context: Context, containerView: View, text: String, @ColorRes color: Int) {
        val snackbar = Snackbar
            .make(containerView, text, Snackbar.LENGTH_LONG)
        context.let { ctx ->
            snackbar.view.setBackgroundColor(
                ContextCompat.getColor(ctx, color)
            )
        }
        snackbar.show()
    }
}
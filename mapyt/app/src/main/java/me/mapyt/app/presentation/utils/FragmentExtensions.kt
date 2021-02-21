package me.mapyt.app.presentation.utils

import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import me.mapyt.app.R
import timber.log.Timber

interface AppFragmentBase {}

fun <T> T.toggleProgress(
    rootView: View,
    isVisible: Boolean,
) where T : Fragment, T : AppFragmentBase {
    val progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
    progressBar?.let {
        if (isVisible) {
            it.visibility = View.VISIBLE
            activity?.setTouchEnabled(false)
            return
        }
        it.visibility = View.GONE
        activity?.setTouchEnabled(true)
    } ?: run {
        Timber.e("null progressBar")
        return
    }
}
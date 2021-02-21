package me.mapyt.app.presentation.binding

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

//ref: https://android.googlesource.com/platform/frameworks/data-binding/+/refs/heads/master/extensions/baseAdapters/src/main/java/android/databinding/adapters/SearchViewBindingAdapter.java
object SearchViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("onQueryTextChange")
    fun setonQueryTextChangeListener(view: SearchView, listener: OnQueryTextChange?) {
        setBaseListener(view, null, listener)
    }

    @JvmStatic
    @BindingAdapter("onQueryTextSubmit")
    fun setOnQueryTextSubmitListener(view: SearchView, listener: OnQueryTextSubmit?) {
        setBaseListener(view, listener, null)
    }

    @JvmStatic
    @BindingAdapter("onQueryTextSubmit", "onQueryTextChange")
    fun setBaseListener(
        view: SearchView, submit: OnQueryTextSubmit?, change: OnQueryTextChange?,
    ) {
        if (submit == null && change == null) {
            view.setOnQueryTextListener(null)
        } else {
            view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return submit?.onSearchQuerySubmit(query) ?: false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return change?.onSearchQueryChange(newText) ?: false
                }
            })
        }
    }

    interface OnQueryTextSubmit {
        fun onSearchQuerySubmit(query: String?): Boolean
    }

    interface OnQueryTextChange {
        fun onSearchQueryChange(newText: String?): Boolean
    }
}

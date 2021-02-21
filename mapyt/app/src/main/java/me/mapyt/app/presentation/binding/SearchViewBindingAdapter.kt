package me.mapyt.app.presentation.binding

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

//ref: https://android.googlesource.com/platform/frameworks/data-binding/+/refs/heads/master/extensions/baseAdapters/src/main/java/android/databinding/adapters/SearchViewBindingAdapter.java
object SearchViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("onQueryTextChange")
    fun setonQueryTextChangeListener(view: SearchView, listener: OnSearchQueryChange?) {
        setBaseListener(view, null, listener)
    }

    @JvmStatic
    @BindingAdapter("onQueryTextSubmit")
    fun setOnQueryTextSubmitListener(view: SearchView, listener: OnSearchQuerySubmit?) {
        setBaseListener(view, listener, null)
    }

    @JvmStatic
    @BindingAdapter("onQueryTextSubmit", "onQueryTextChange")
    fun setBaseListener(
        view: SearchView, submit: OnSearchQuerySubmit?, change: OnSearchQueryChange?,
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

    interface OnSearchQuerySubmit {
        fun onSearchQuerySubmit(query: String?): Boolean
    }

    interface OnSearchQueryChange {
        fun onSearchQueryChange(newText: String?): Boolean
    }
}

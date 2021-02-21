package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.mapyt.app.presentation.binding.SearchViewBindingAdapter
import timber.log.Timber

class PlacesSearchViewModel: ViewModel(), SearchViewBindingAdapter.OnQueryTextSubmit,
    SearchViewBindingAdapter.OnQueryTextChange {
    private val _currentKeywords = MutableLiveData<String?>(null)
    val currentModel: LiveData<String?> = _currentKeywords

    fun start() {
        //TODO: podrian cargarse lugares con la ubicacion por defecto
    }

    private fun onSubmitKeywords(keywords: String?) {
        Timber.d("New keywords: %s", keywords)
        _currentKeywords.value = keywords
    }

    override fun onSearchQuerySubmit(query: String?): Boolean {
        onSubmitKeywords(query)
        return false
    }

    override fun onSearchQueryChange(newText: String?): Boolean {
        if(newText.isNullOrEmpty()) {
            Timber.d("text cleared ${newText == ""}")
        }
        return false
    }
}
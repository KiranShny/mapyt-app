package me.mapyt.app.presentation.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.mapyt.app.R
import me.mapyt.app.presentation.binding.SearchViewBindingAdapter
import me.mapyt.app.presentation.utils.Event
import timber.log.Timber

class PlacesSearchViewModel : ViewModel(), SearchViewBindingAdapter.OnQueryTextSubmit,
    SearchViewBindingAdapter.OnQueryTextChange {
    private val _currentKeywords = MutableLiveData<String?>(null)
    val currentModel: LiveData<String?> = _currentKeywords

    private val _loadDefaultPosition = MutableLiveData<Event<UserPosition>>()
    val loadDefaultPosition : LiveData<Event<UserPosition>> get() = _loadDefaultPosition

    override fun onSearchQuerySubmit(query: String?): Boolean {
        onSubmitKeywords(query)
        return false
    }

    override fun onSearchQueryChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()) {
            Timber.d("text cleared ${newText == ""}")
        }
        return false
    }

    fun start() {
        //TODO: podrian cargarse lugares con la ubicacion por defecto
    }

    fun onMapReady() {
        _loadDefaultPosition.value = Event(getDefaultPosition())
    }

    private fun onSubmitKeywords(keywords: String?) {
        Timber.d("New keywords: %s", keywords)
        _currentKeywords.value = keywords
    }

    private fun getDefaultPosition() =
        UserPosition(12.10629123798485, -86.24891870346221, 16.0F, R.string.you_are_here)
}

data class UserPosition(
    val lat: Double,
    val lng: Double,
    val zoomLevel: Float,
    @StringRes val messageId: Int,
)
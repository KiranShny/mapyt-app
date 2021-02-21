package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.usecases.NearbyPlacesSearchParams
import me.mapyt.app.core.domain.usecases.SearchNearbyPlacesUseCase
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.throwable
import me.mapyt.app.presentation.binding.SearchViewBindingAdapter
import me.mapyt.app.presentation.utils.Event
import me.mapyt.app.presentation.utils.toMapPlace
import me.mapyt.app.presentation.viewmodels.PlacesSearchViewModel.SearchPlacesState.*
import me.mapyt.app.utils.uniqueId
import timber.log.Timber

class PlacesSearchViewModel(private val searchPlacesUseCase: SearchNearbyPlacesUseCase) :
    ViewModel(), SearchViewBindingAdapter.OnSearchQuerySubmit,
    SearchViewBindingAdapter.OnSearchQueryChange {
    private var currentUserPosition: UserPosition = getDefaultPosition()
    private var currentPlaces: List<Place>? = null

    private val _placesEvents = MutableLiveData<Event<SearchPlacesState>>()
    val placesEvents: LiveData<Event<SearchPlacesState>> get() = _placesEvents

    private val _loadUserPosition = MutableLiveData<Event<UserPosition>>()
    val loadUserPosition: LiveData<Event<UserPosition>> get() = _loadUserPosition

    private val _navigateToPlaceDetails = MutableLiveData<Event<MapPlace>>()
    val navigateToPlaceDetails: LiveData<Event<MapPlace>> get() = _navigateToPlaceDetails

    override fun onSearchQuerySubmit(query: String?): Boolean {
        onSubmitKeywords(query)
        return false
    }

    override fun onSearchQueryChange(newText: String?): Boolean {
        //TODO: con newText.isNullOrEmpty() se podria validar si no hay query ingresada y
        //volver al estado original de la pantalla
        return false
    }

    fun start() {
        //TODO: podrian cargarse lugares con la ubicacion por defecto
    }

    fun onMapReady() {
        triggerLoadUserPosition()
    }

    fun onMapCleared() {
        triggerLoadUserPosition()
    }

    fun onNewCoordsSelected(lat: Double, lng: Double) {
        currentUserPosition = currentUserPosition.copy(lat = lat, lng = lng)
        triggerLoadUserPosition()
    }

    fun onMapPlaceDetailsSelected(selectedPlaceCode: String) {
        val selectedPlace = currentPlaces?.firstOrNull { it.placeId == selectedPlaceCode }
        selectedPlace?.let {
            _navigateToPlaceDetails.value = Event(it.toMapPlace())
        } ?: run {
            Timber.e("selected place not found")
        }
    }

    private fun onSubmitKeywords(keywords: String?) {
        Timber.d("new keywords: %s", keywords)
        searchPlaces(keywords, currentUserPosition)
    }

    private fun searchPlaces(keywords: String?, userPosition: UserPosition) {
        _placesEvents.value = Event(ShowLoading)
        //TODO: mover scope a use case
        viewModelScope.launch(Dispatchers.IO) {
            val searchResult = searchPlacesUseCase(NearbyPlacesSearchParams(
                keywords = splitKeywords(keywords),
                location = mergeCoords(userPosition.lat, userPosition.lng),
            ))
            withContext(Dispatchers.Main) {
                when (searchResult) {
                    is ResultOf.Success<List<Place>> -> {
                        currentPlaces = searchResult.value
                        _placesEvents.value = Event(HideLoading)
                        _placesEvents.value = Event(LoadPlaces(
                            searchResult.value.map { it.toMapPlace() }
                        ))
                    }
                    else -> {
                        _placesEvents.value = Event(HideLoading)
                        _placesEvents.value =
                            Event(ShowPlacesError(searchResult.throwable))
                    }
                }
            }
        }
    }

    private fun triggerLoadUserPosition(userPosition: UserPosition = currentUserPosition) {
        _loadUserPosition.value = Event(userPosition)
    }

    private fun getDefaultPosition() =
        UserPosition(
            uniqueId(),
            12.10629123798485,
            -86.24891870346221,
            DEFAULT_ZOOM
        )

    private fun splitKeywords(keywords: String?) =
        keywords?.split(DEFAULT_KEYWORD_SEPARATOR)?.map { it.trim() } ?: emptyList()

    private fun mergeCoords(latitude: Double, longitude: Double) = "${latitude},${longitude}"

    sealed class SearchPlacesState {
        data class LoadPlaces(val places: List<MapPlace>) : SearchPlacesState()
        data class ShowPlacesError(val error: Throwable) : SearchPlacesState()
        object HideLoading : SearchPlacesState()
        object ShowLoading : SearchPlacesState()
    }

    companion object {
        const val DEFAULT_ZOOM = 16.0F
        const val DEFAULT_KEYWORD_SEPARATOR = ","
    }
}
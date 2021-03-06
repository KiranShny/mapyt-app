package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.domain.usecases.GetSavedPlacesUseCase
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.throwable
import me.mapyt.app.presentation.utils.Event
import me.mapyt.app.presentation.viewmodels.SavedPlacesViewModel.SavedPlacesState.*
import me.mapyt.app.presentation.viewmodels.SavedPlacesViewModel.SelectedPlaceState.ShowPlaceDetails

class SavedPlacesViewModel(private val getSavedPlacesUseCase: GetSavedPlacesUseCase) : ViewModel() {

    private val _placesEvents = MutableLiveData<Event<SavedPlacesState>>()
    val placesEvents: LiveData<Event<SavedPlacesState>> get() = _placesEvents

    private val _selectedPlaceEvents = MutableLiveData<Event<SelectedPlaceState>>()
    val selectedPlaceEvents: LiveData<Event<SelectedPlaceState>> get() = _selectedPlaceEvents

    fun start() {
        loadSavedPlaces()
    }

    fun onPlaceSelected(placeDetails: PlaceDetails) {
        val placeMaster = placeDetails.toMaster()
        _selectedPlaceEvents.value = Event(ShowPlaceDetails(placeMaster))
    }

    private fun loadSavedPlaces() {
        _placesEvents.value = Event(LoadingPlaces(true))
        viewModelScope.launch {
            when (val placesResult = getSavedPlacesUseCase()) {
                is ResultOf.Success<List<PlaceDetails>> -> {
                    _placesEvents.value = Event(LoadingPlaces(false))
                    _placesEvents.value = Event(LoadPlaces(placesResult.value))
                }
                else -> {
                    _placesEvents.value = Event(LoadingPlaces(false))
                    _placesEvents.value = Event(ShowPlacesError(placesResult.throwable))
                }
            }
        }
    }

    sealed class SavedPlacesState {
        data class LoadPlaces(val places: List<PlaceDetails>) : SavedPlacesState()
        data class ShowPlacesError(val error: Throwable) : SavedPlacesState()
        data class LoadingPlaces(val isLoading: Boolean) : SavedPlacesState()
    }

    sealed class SelectedPlaceState {
        data class ShowPlaceDetails(val master: MapPlace) : SelectedPlaceState()
    }
}
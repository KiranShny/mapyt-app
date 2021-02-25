package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.domain.usecases.GetPlaceDetailsUseCase
import me.mapyt.app.core.domain.usecases.GetPlacePhotoUseCase
import me.mapyt.app.core.domain.usecases.NearbyPlacesSearchParams
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.throwable
import me.mapyt.app.presentation.utils.Event
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.*
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceDetailsState.*
import timber.log.Timber

class PlaceDetailsViewModel(
    private val getPhotoPathUseCase: GetPlacePhotoUseCase,
    private val getDetailsUseCase: GetPlaceDetailsUseCase,
) : ViewModel() {
    private val _placeMaster = MutableLiveData<MapPlace?>()
    val placeMaster: LiveData<MapPlace?> get() = _placeMaster

    private val _masterEvents = MutableLiveData<Event<PlaceMasterState>>()
    val masterEvents: LiveData<Event<PlaceMasterState>> get() = _masterEvents

    private val _detailsEvents = MutableLiveData<Event<PlaceDetailsState>>()
    val detailsEvents: LiveData<Event<PlaceDetailsState>> get() = _detailsEvents

    private val _coverImagePath = MutableLiveData<String>()
    val coverImagePath: LiveData<String> get() = _coverImagePath

    //TODO: fix para pasar el param al vm sin inyeccion + avanzada, refactorizar
    fun start(master: MapPlace?) {
        master?.let {
            _placeMaster.value = it
            _masterEvents.value = Event(LoadMaster(it))
            _coverImagePath.value = getCoverPhotoPath(it)

            getDetails(it)
        } ?: run {
            _masterEvents.value = Event(ShowMasterError(IllegalArgumentException("master")))
        }
    }

    private fun getCoverPhotoPath(mapPlace: MapPlace?): String? {
        mapPlace?.let { master ->
            val coverPath = master.photosRefs?.first() ?: return null
            val photoPathResult = getPhotoPathUseCase(coverPath)
            return when (photoPathResult) {
                is ResultOf.Success -> photoPathResult.value
                is ResultOf.Failure -> {
                    //por el momento la excepcion es silenciosa, se cargaria placeholder
                    Timber.e(photoPathResult.throwable)
                    null
                }
            }
        } ?: kotlin.run {
            return null
        }
    }

    private fun getDetails(master: MapPlace) {
        val placeId = master.code
        _detailsEvents.value = Event(ShowLoadingDetails)

        //TODO: mover scope a use case
        viewModelScope.launch(Dispatchers.IO) {
            val detailsResult = getDetailsUseCase(placeId)
            withContext(Dispatchers.Main) {
                when (detailsResult) {
                    is ResultOf.Success<PlaceDetails> -> {
                        _detailsEvents.value = Event(HideLoadingDetails)
                        _detailsEvents.value = Event(LoadDetails(detailsResult.value))
                    }
                    else -> {
                        _detailsEvents.value =
                            Event(HideLoadingDetails)
                        _detailsEvents.value =
                            Event(ShowDetailsError(
                                detailsResult.throwable))
                    }
                }
            }
        }
    }

    sealed class PlaceMasterState {
        data class LoadMaster(val master: MapPlace) : PlaceMasterState()
        data class ShowMasterError(val error: Throwable) : PlaceMasterState()
    }

    sealed class PlaceDetailsState {
        data class LoadDetails(val details: PlaceDetails) : PlaceDetailsState()
        data class ShowDetailsError(val error: Throwable) : PlaceDetailsState()
        object HideLoadingDetails : PlaceDetailsState()
        object ShowLoadingDetails : PlaceDetailsState()
    }

}
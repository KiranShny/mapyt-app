package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.mapyt.app.R
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.domain.usecases.*
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.throwable
import me.mapyt.app.core.shared.valueOrThrow
import me.mapyt.app.presentation.exceptions.ViewModelOperationException
import me.mapyt.app.presentation.utils.Event
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceDetailsState.*
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.LoadMaster
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.ShowMasterError
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.SavedPlaceState.*
import timber.log.Timber

class PlaceDetailsViewModel(
    private val getPhotoPathUseCase: GetPlacePhotoUseCase,
    private val getDetailsUseCase: GetPlaceDetailsUseCase,
    private val toggleSavePlaceUseCase: ToggleSavePlaceUseCase,
    private val isSavedPlaceUseCase: IsSavedPlaceUseCase,
) : ViewModel() {
    private val _placeMaster = MutableLiveData<MapPlace?>()
    val placeMaster: LiveData<MapPlace?> get() = _placeMaster

    private val _placeDetails = MutableLiveData<PlaceDetails?>()
    val placeDetails: LiveData<PlaceDetails?> get() = _placeDetails

    private val _masterEvents = MutableLiveData<Event<PlaceMasterState>>()
    val masterEvents: LiveData<Event<PlaceMasterState>> get() = _masterEvents

    private val _detailsEvents = MutableLiveData<Event<PlaceDetailsState>>()
    val detailsEvents: LiveData<Event<PlaceDetailsState>> get() = _detailsEvents

    private val _saveEvents = MutableLiveData<Event<SavedPlaceState>>()
    val saveEvents: LiveData<Event<SavedPlaceState>> get() = _saveEvents

    private val _coverImagePath = MutableLiveData<String>()
    val coverImagePath: LiveData<String> get() = _coverImagePath

    private val _isPlaceSaved = MutableLiveData<Boolean>()
    val isPlaceSaved: LiveData<Boolean> get() = _isPlaceSaved

    //TODO: fix para pasar el param al vm sin inyeccion + avanzada, refactorizar
    fun start(master: MapPlace?) {
        master?.let {
            _placeMaster.value = it
            _masterEvents.value = Event(LoadMaster(it))
            _coverImagePath.value = getCoverPhotoPath(it)

            getDetails(it)
            _isPlaceSaved.value = false
            setIsPlaceSaved(it)
        } ?: run {
            _masterEvents.value = Event(ShowMasterError(IllegalArgumentException("master")))
        }
    }

    fun trySavePlace() {
        _placeDetails.value?.let { details ->
            toggleSavePlace(details, _isPlaceSaved.value ?: false)
        } ?: run {
            _detailsEvents.value = Event(
                ShowDetailsError(ViewModelOperationException(R.string.trying_save_place_error))
            )
        }
    }

    private fun getCoverPhotoPath(mapPlace: MapPlace?): String? {
        mapPlace?.let { master ->
            val coverPath = master.photosRefs?.first() ?: return null
            return when (val photoPathResult = getPhotoPathUseCase(coverPath)) {
                is ResultOf.Success -> photoPathResult.value
                is ResultOf.Failure -> {
                    //por el momento la excepcion es silenciosa, se cargaria placeholder
                    Timber.e(photoPathResult.throwable)
                    null
                }
            }
        } ?: run {
            return null
        }
    }

    private fun getDetails(master: MapPlace) {
        val placeId = master.code
        _detailsEvents.value = Event(ShowLoadingDetails)

        viewModelScope.launch {
            when (val detailsResult = getDetailsUseCase(placeId)) {
                is ResultOf.Success<PlaceDetails> -> {
                    _placeDetails.value = detailsResult.value
                    _detailsEvents.value = Event(HideLoadingDetails)
                    _detailsEvents.value = Event(LoadDetails(detailsResult.value))
                }
                else -> {
                    _detailsEvents.value = Event(HideLoadingDetails)
                    _detailsEvents.value = Event(ShowDetailsError(detailsResult.throwable))
                }
            }
        }
    }

    private fun toggleSavePlace(place: PlaceDetails, isSaved: Boolean) {
        _saveEvents.value = Event(ShowSavingPlace)
        viewModelScope.launch {
            when (val saveResult = toggleSavePlaceUseCase(place, isSaved)) {
                is ResultOf.Success<SavedPlaceCaseState> -> {
                    _saveEvents.value = Event(HideSavingPlace)

                    when (saveResult.value) {
                        SavedPlaceCaseState.SAVED -> {
                            _saveEvents.value = Event(OnPlaceSaved(place))
                            _isPlaceSaved.value = true
                        }
                        SavedPlaceCaseState.DELETED -> {
                            _saveEvents.value = Event(OnPlaceDeleted(place))
                            _isPlaceSaved.value = false
                        }
                    }
                }
                else -> {
                    _saveEvents.value = Event(HideSavingPlace)
                    _saveEvents.value = Event(OnSavePlaceError(saveResult.throwable))
                    _isPlaceSaved.value = false
                }
            }
        }
    }

    private fun setIsPlaceSaved(master: MapPlace) {
        _saveEvents.value = Event(ShowSavingPlace)
        viewModelScope.launch {
            when (val isSavedResult = isSavedPlaceUseCase(master.code)) {
                is ResultOf.Success<Boolean> -> {
                    _saveEvents.value = Event(HideSavingPlace)
                    _isPlaceSaved.value = isSavedResult.valueOrThrow
                    Timber.d("Is saved place? ${isSavedResult.value}")
                }
                else -> {
                    _saveEvents.value = Event(HideSavingPlace)
                    Timber.e(isSavedResult.throwable)
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

    sealed class SavedPlaceState {
        data class OnPlaceSaved(val details: PlaceDetails) : SavedPlaceState()
        data class OnPlaceDeleted(val details: PlaceDetails) : SavedPlaceState()
        data class OnSavePlaceError(val error: Throwable) : SavedPlaceState()
        object ShowSavingPlace : SavedPlaceState()
        object HideSavingPlace : SavedPlaceState()
    }

}
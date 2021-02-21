package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.mapyt.app.core.domain.usecases.GetPlacePhotoUseCase
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.presentation.utils.Event
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.LoadMaster
import me.mapyt.app.presentation.viewmodels.PlaceDetailsViewModel.PlaceMasterState.ShowMasterError
import timber.log.Timber

class PlaceDetailsViewModel(private val getPhotoPathUseCase: GetPlacePhotoUseCase) : ViewModel() {
    private val _placeMaster = MutableLiveData<MapPlace?>()
    val placeMaster: LiveData<MapPlace?> get() = _placeMaster

    private val _masterEvents = MutableLiveData<Event<PlaceMasterState>>()
    val masterEvents: LiveData<Event<PlaceMasterState>> get() = _masterEvents

    private val _coverImagePath = MutableLiveData<String>()
    val coverImagePath: LiveData<String> get() = _coverImagePath

    //TODO: fix para pasar el param al vm sin inyeccion + avanzada, refactorizar
    fun start(master: MapPlace?) {
        master?.let {
            _placeMaster.value = it
            _masterEvents.value = Event(LoadMaster(it))
            _coverImagePath.value = getCoverPhotoPath(it)
            Timber.d(_coverImagePath.value)
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

    sealed class PlaceMasterState {
        data class LoadMaster(val master: MapPlace) : PlaceMasterState()
        data class ShowMasterError(val error: Throwable) : PlaceMasterState()
    }

}
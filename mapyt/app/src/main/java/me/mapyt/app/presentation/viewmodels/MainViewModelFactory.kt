package me.mapyt.app.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.mapyt.app.MapytApp
import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.usecases.*
import me.mapyt.app.platform.database.sources.PlacesLocalSourceImpl
import me.mapyt.app.platform.networking.places.ApiClient
import me.mapyt.app.platform.networking.places.PlacesRemoteSourceImpl
import java.lang.IllegalArgumentException


class MainViewModelFactory(private val application: Application?) : ViewModelProvider.Factory {

    //TODO: reemplazar con Koin
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(application == null) {
            throw IllegalArgumentException("Invalid Application")
        }

        val dbContainer = (application as MapytApp).dbContainer
        val placeDao = dbContainer.appDb.placeDao()

        if (PlacesSearchViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(
                SearchNearbyPlacesUseCase::class.java,
            )
                .newInstance(
                    SearchNearbyPlacesUseCase(
                        PlacesRepository(
                            PlacesRemoteSourceImpl(ApiClient.service),
                            PlacesLocalSourceImpl(placeDao)
                        )
                    ),
                )
        }
        if (PlaceDetailsViewModel::class.java.isAssignableFrom(modelClass)) {
            val repository = PlacesRepository(
                PlacesRemoteSourceImpl(ApiClient.service),
                PlacesLocalSourceImpl(placeDao)
                )
            return modelClass
                .getConstructor(
                    GetPlacePhotoUseCase::class.java,
                    GetPlaceDetailsUseCase::class.java,
                    ToggleSavePlaceUseCase::class.java,
                    IsSavedPlaceUseCase::class.java
                )
                .newInstance(
                    GetPlacePhotoUseCase(repository),
                    GetPlaceDetailsUseCase(repository),
                    ToggleSavePlaceUseCase(repository),
                    IsSavedPlaceUseCase(repository)
                )
        }

        throw IllegalStateException("El ViewModel solicitado no fue encontrado");
    }
}
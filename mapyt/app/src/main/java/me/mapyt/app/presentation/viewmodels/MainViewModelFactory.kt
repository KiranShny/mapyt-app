package me.mapyt.app.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.usecases.SearchNearbyPlacesUseCase
import me.mapyt.app.platform.networking.places.ApiClient
import me.mapyt.app.platform.networking.places.PlacesRemoteSourceImpl

object MainViewModelFactory : ViewModelProvider.Factory {
    //TODO: reemplazar con Koin
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (PlacesSearchViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(
                SearchNearbyPlacesUseCase::class.java,
            )
                .newInstance(
                    SearchNearbyPlacesUseCase(
                        PlacesRepository(
                            PlacesRemoteSourceImpl(ApiClient.service)
                        )
                    ),
                )
        }

        throw IllegalStateException("El ViewModel solicitado no fue encontrado");
    }
}
package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.shared.ResultOf

class GetSavedPlacesUseCase(private val repository: PlacesRepository,) {
    suspend operator fun invoke(): ResultOf<List<PlaceDetails>> {
        return try {
            ResultOf.Success(repository.getSavedPlaces())
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }
}
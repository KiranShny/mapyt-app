package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.shared.ResultOf

class SavePlaceUseCase(private val repository: PlacesRepository) {
    suspend operator fun invoke(place: PlaceDetails): ResultOf<Boolean> {
        return try {
            val result = repository.savePlace(place)
            ResultOf.Success(true)
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }
}
package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.validators.PlacePhotoValidator
import me.mapyt.app.core.shared.ResultOf

class IsSavedPlaceUseCase(
    private val repository: PlacesRepository,
    private val validator: PlacePhotoValidator = PlacePhotoValidator(),
) {
    suspend operator fun invoke(placeId: String): ResultOf<Boolean> {
        return try {
            val result = repository.existsSavedPlace(placeId)
            ResultOf.Success(result)
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }

}
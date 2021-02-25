package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.domain.validators.PlaceDetailsValidator
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.isFailure
import me.mapyt.app.core.shared.throwable

class GetPlaceDetailsUseCase(
    private val repository: PlacesRepository,
    private val validator: PlaceDetailsValidator = PlaceDetailsValidator(),
) {
    suspend operator fun invoke(placeId: String): ResultOf<PlaceDetails> {
        val validationResult = validator(placeId)
        if(validationResult.isFailure) return ResultOf.Failure(validationResult.throwable)

        return try {
            ResultOf.Success(repository.getDetails(placeId))
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }
}
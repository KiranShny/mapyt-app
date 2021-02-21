package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.validators.PlacePhotoValidator
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.isFailure
import me.mapyt.app.core.shared.throwable

class GetPlacePhotoUseCase(
    private val repository: PlacesRepository,
    private val validator: PlacePhotoValidator = PlacePhotoValidator(),
) {
    operator fun invoke(photoReference: String): ResultOf<String> {
        val validationResult = validator(photoReference)
        if(validationResult.isFailure) return ResultOf.Failure(validationResult.throwable)

        return try {
            ResultOf.Success(repository.getPhotoPath(photoReference))
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }

}
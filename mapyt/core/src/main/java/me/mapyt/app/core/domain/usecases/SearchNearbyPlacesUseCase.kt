package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.usecases.SearchNearbyPlacesUseCase.Companion.DEFAULT_RADIUS_METERS
import me.mapyt.app.core.domain.validators.NearbyPlacesSearchValidator
import me.mapyt.app.core.shared.ResultOf
import me.mapyt.app.core.shared.isFailure
import me.mapyt.app.core.shared.throwable

class SearchNearbyPlacesUseCase(
    private val repository: PlacesRepository,
    //TODO: inject it (?)
    private val validator: NearbyPlacesSearchValidator = NearbyPlacesSearchValidator()
) {

    companion object {
        const val DEFAULT_RADIUS_METERS = 100
    }

    suspend operator fun invoke(params: NearbyPlacesSearchParams): ResultOf<List<Place>> {
        val validationResult = validator(params)
        if(validationResult.isFailure) return ResultOf.Failure(validationResult.throwable)

        return try {
            val list = repository.searchNearby(params.keywords, params.location, params.radius)
            ResultOf.Success(list)
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }

}

data class NearbyPlacesSearchParams(
    val keywords: List<String>?,
    val location: String,
    val radius: Int = DEFAULT_RADIUS_METERS,
    val allowEmptyKeywords: Boolean = false,
)
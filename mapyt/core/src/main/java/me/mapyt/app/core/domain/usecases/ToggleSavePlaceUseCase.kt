package me.mapyt.app.core.domain.usecases

import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.entities.PlaceDetails
import me.mapyt.app.core.shared.ResultOf

class ToggleSavePlaceUseCase(private val repository: PlacesRepository) {
    suspend operator fun invoke(place: PlaceDetails, isCurrentlySaved: Boolean): ResultOf<SavedPlaceCaseState> {
        return try {
            if(isCurrentlySaved) {
                repository.deletePlace(place)
                ResultOf.Success(SavedPlaceCaseState.DELETED)
            } else {
                repository.savePlace(place)
                ResultOf.Success(SavedPlaceCaseState.SAVED)
            }
        } catch (error: Throwable) {
            ResultOf.Failure(error)
        }
    }
}

enum class SavedPlaceCaseState {
    SAVED, DELETED
}
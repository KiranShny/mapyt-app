package me.mapyt.app.core.domain.validators

import me.mapyt.app.core.domain.exceptions.DomainValidationException
import me.mapyt.app.core.shared.ResultOf

class PlacePhotoValidator {

    operator fun invoke(photoReference: String?): ResultOf<Boolean> {
        return photoReference?.let {
            if (it.isEmpty() || it.length < 2)
                return ResultOf.Failure(DomainValidationException("Referencia de foto inválida"))

            return ResultOf.Success(true)
        } ?: run {
            ResultOf.Failure(DomainValidationException("Parámetros de foto inválidos"))
        }
    }
}
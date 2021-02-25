package me.mapyt.app.core.domain.validators

import me.mapyt.app.core.domain.exceptions.DomainValidationException
import me.mapyt.app.core.shared.ResultOf

class PlaceDetailsValidator {
    operator fun invoke(placeId: String?): ResultOf<Boolean> {
        return placeId?.let {
            if (it.isEmpty() || it.length < 2)
                return ResultOf.Failure(DomainValidationException("ID de lugar inválido"))
            return ResultOf.Success(true)
        } ?: run {
            ResultOf.Failure(DomainValidationException("Parámetros de detalle de lugar inválidos"))
        }
    }
}
package me.mapyt.app.core.domain.validators

import me.mapyt.app.core.domain.exceptions.DomainValidationException
import me.mapyt.app.core.domain.usecases.NearbyPlacesSearchParams
import me.mapyt.app.core.shared.ResultOf

class NearbyPlacesSearchValidator {

    operator fun invoke(params: NearbyPlacesSearchParams?): ResultOf<Boolean> {
        return params?.let {
            if (it.keywords.isEmpty())
                return ResultOf.Failure(DomainValidationException("Favor ingrese al menos una palabra clave"))
            //TODO: validar formato de coordenadas
            if (it.location.trim().isEmpty())
                return ResultOf.Failure(DomainValidationException("No se ha logrado obtener la ubicación"))
            if (it.radius <= 0)
                return ResultOf.Failure(DomainValidationException("Radio inválido"))

            return ResultOf.Success(true)
        } ?: run {
            ResultOf.Failure(DomainValidationException("Parámetros inválidos"))
        }
    }
}
package me.mapyt.app.core.domain.validators

import me.mapyt.app.core.domain.exceptions.DomainValidationException
import me.mapyt.app.core.domain.usecases.NearbyPlacesSearchParams
import me.mapyt.app.core.shared.ResultOf

class NearbyPlacesSearchValidator {

    operator fun invoke(params: NearbyPlacesSearchParams?): ResultOf<Boolean> {
        return params?.let {
            if (!it.allowEmptyKeywords && it.keywords == null)
                return ResultOf.Failure(DomainValidationException("Favor ingrese al menos una palabra clave"))
            it.keywords?.let { keywords ->
                if (keywords.isEmpty())
                    return ResultOf.Failure(DomainValidationException("Favor ingrese al menos una palabra clave"))
                if (keywords.any { keyword -> keyword.length < 2 })
                    return ResultOf.Failure(DomainValidationException("Las palabras claves deben tener al menos 2 caracteres"))
            }
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
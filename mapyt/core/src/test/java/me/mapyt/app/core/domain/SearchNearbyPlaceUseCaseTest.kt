package me.mapyt.app.core.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.mapyt.app.core.data.PlacesRepository
import me.mapyt.app.core.domain.entities.Place
import me.mapyt.app.core.domain.entities.PlaceGeometry
import me.mapyt.app.core.domain.entities.PlaceLocation
import me.mapyt.app.core.domain.exceptions.DomainValidationException
import me.mapyt.app.core.domain.usecases.NearbyPlacesSearchParams
import me.mapyt.app.core.domain.usecases.SearchNearbyPlacesUseCase
import me.mapyt.app.core.shared.isFailure
import me.mapyt.app.core.shared.isSuccessful
import me.mapyt.app.core.shared.throwable
import me.mapyt.app.core.shared.valueOrThrow
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchNearbyPlaceUseCaseTest {

    @Test
    fun `given searchNearby() with valid params should return places`() {
        val expectedList = listOf<Place>(defPlaceFixture)
        val providedParams = NearbyPlacesSearchParams(listOf("food"), "0,0")
        val repository = mockk<PlacesRepository>()
        coEvery { repository.searchNearby(any(), any(), any()) } returns expectedList

        val sut = SearchNearbyPlacesUseCase(repository)
        val result = runBlocking { sut(providedParams) }

        assertTrue(result.throwable.message, result.isSuccessful)
        val actualPlaces = result.valueOrThrow
        assertTrue(actualPlaces.isNotEmpty())
    }

    @Test
    fun `given searchNearby() with empty keywords should return error`() {
        val expectedList = listOf<Place>(defPlaceFixture)
        val providedParams = NearbyPlacesSearchParams(emptyList(), "0,0")
        val repository = mockk<PlacesRepository>()
        coEvery { repository.searchNearby(any(), any(), any()) } returns expectedList

        val sut = SearchNearbyPlacesUseCase(repository)
        val result = runBlocking { sut(providedParams) }

        assertTrue(result.isFailure)
        assertNotNull(result.throwable)
        assertTrue(result.throwable is DomainValidationException)
    }

    @Test
    fun `given searchNearby() with empty location should return error`() {
        val expectedList = listOf<Place>(defPlaceFixture)
        val providedParams = NearbyPlacesSearchParams(listOf("food"), " ")
        val repository = mockk<PlacesRepository>()
        coEvery { repository.searchNearby(any(), any(), any()) } returns expectedList

        val sut = SearchNearbyPlacesUseCase(repository)
        val result = runBlocking { sut(providedParams) }

        assertTrue(result.isFailure)
        assertNotNull(result.throwable)
        assertTrue(result.throwable is DomainValidationException)
    }

    @Test
    fun `given searchNearby() with negative radius should return error`() {
        val expectedList = listOf<Place>(defPlaceFixture)
        val providedParams = NearbyPlacesSearchParams(listOf("food"), "0,0", -1)
        val repository = mockk<PlacesRepository>()
        coEvery { repository.searchNearby(any(), any(), any()) } returns expectedList

        val sut = SearchNearbyPlacesUseCase(repository)
        val result = runBlocking { sut(providedParams) }

        assertTrue(result.isFailure)
        assertNotNull(result.throwable)
        assertTrue(result.throwable is DomainValidationException)
    }

    private val defPlaceFixture =
        Place(PlaceGeometry(PlaceLocation(0.0, 0.0)), "", "", "", 0.0, 0.0, emptyList())

}
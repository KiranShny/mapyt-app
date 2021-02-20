package me.mapyt.app.platform.networking

import kotlinx.coroutines.runBlocking
import me.mapyt.app.core.shared.InvalidResponseException
import me.mapyt.app.platform.networking.places.PlacesRemoteSourceImpl
import me.mapyt.app.platform.networking.places.PlacesService
import me.mapyt.app.platform.utils.ApiTestHelper
import me.mapyt.app.platform.utils.enqueueResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlacesRemoteSourceTest {
    private lateinit var mockServer: MockWebServer
    private lateinit var apiCore: Retrofit
    private lateinit var service: PlacesService

    @Before
    fun setup() {
        mockServer = MockWebServer()
        apiCore = ApiTestHelper.buildRetrofitForMockServer(mockServer)
        service = apiCore.create(PlacesService::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun `given api 200 response with valid params should return places`() {
        mockServer.enqueueResponse("nearbysearch-200.json", 200)
        val remoteSource = PlacesRemoteSourceImpl(service)

        runBlocking {
            val actual = remoteSource.searchNearby("0,0", 0)
            assertNotNull(actual)
            assertTrue(actual.isNotEmpty())

            //TODO: implementar assertions para la lista
            with(actual.first()) {
                assertNotNull(name)
                assertNotNull(placeId)
                assertNotNull(geometry)
                assertNotNull(geometry.location)
            }
        }
    }

    @Test(expected = InvalidResponseException::class)
    fun `given api 200 response with invalid params should return error`() {
        mockServer.enqueueResponse("nearbysearch-invalid-200.json", 200)
        val remoteSource = PlacesRemoteSourceImpl(service)

        runBlocking {
            val actual = remoteSource.searchNearby("0,0", 0)
        }
    }
}
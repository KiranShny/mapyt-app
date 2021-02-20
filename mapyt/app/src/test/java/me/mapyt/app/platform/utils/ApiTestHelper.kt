package me.mapyt.app.platform.utils

import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiTestHelper {
    internal fun buildRetrofitForMockServer(mockServer: MockWebServer) = Retrofit.Builder()
        .baseUrl(mockServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
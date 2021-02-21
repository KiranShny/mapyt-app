package me.mapyt.app.platform.networking.places

import me.mapyt.app.BuildConfig
import me.mapyt.app.platform.networking.places.ApiConstants.API_KEY
import me.mapyt.app.platform.networking.places.ApiConstants.BASE_API_URL
import me.mapyt.app.platform.networking.places.ApiConstants.PARAM_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


    private val httpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(
            Interceptor { chain ->
                val originalRequest = chain.request()
                val originalHttpUrl = originalRequest.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(PARAM_KEY, API_KEY)
                    .build()

                val requestBuilder = originalRequest.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        )

        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }

    private val retrofit = Retrofit.Builder()
        .client(httpClient.build())
        .baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(PlacesService::class.java)
}
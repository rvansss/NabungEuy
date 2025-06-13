package com.evan0107.nabungeuy.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://hobi-api.bagasaldianata.my.id/api/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    interface HobiApiService {
        @GET("static-api.json")
        suspend fun getHobiItems(): String
    }

    object HobiApi {
        val service: HobiApiService by lazy {
            retrofit.create(HobiApiService::class.java)
        }
    }

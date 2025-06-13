package com.evan0107.nabungeuy.network

import com.evan0107.nabungeuy.model.HobiItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://hobi-api.bagasaldianata.my.id/api/"

    private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    interface HobiApiService {
        @GET("static-api.json")
        suspend fun getHobiItem(): List<HobiItem>
    }

    object HobiApi {
        val service: HobiApiService by lazy {
            retrofit.create(HobiApiService::class.java)
        }
        fun getHobiItemUrl(imageId: String): String {
            return "$BASE_URL$imageId.jpg"
        }
    }

    enum class ApiStatus {LOADING,SUCCESS}

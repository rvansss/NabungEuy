package com.evan0107.nabungeuy.network

import com.evan0107.nabungeuy.model.HobiResponse
import com.evan0107.nabungeuy.model.SingleHobiResponse // Import SingleHobiResponse yang baru
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Multipart
import retrofit2.http.Path
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private const val BASE_URL = "https://hobi-api.bagasaldianata.my.id/api/"
private const val BASE_IMAGE_URL = "https://hobi-api.bagasaldianata.my.id/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface HobiApiService {
    @GET("hobi-items")
    suspend fun getHobiItems(): HobiResponse

    @Multipart
    @POST("hobi-items")
    suspend fun addHobi(
        @Part("name") name: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ): SingleHobiResponse

    @DELETE("hobi-items/{id}")
    suspend fun deleteHobi(@Path("id") hobiId: Int): SingleHobiResponse

    @Multipart
    @POST("hobi-items/{id}")
    suspend fun updateHobi(
        @Path("id") hobiId: Int,
        @Part("_method") method: RequestBody,
        @Part("name") name: RequestBody,
        @Part("brand") brand: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ): SingleHobiResponse
}

object HobiApi {
    val service: HobiApiService by lazy {
        retrofit.create(HobiApiService::class.java)
    }

    fun getHobiItemImageUrl(imagePath: String?): String? {
        return if (imagePath != null) {
            "$BASE_IMAGE_URL${imagePath.removePrefix("/")}"
        } else {
            null
        }
    }
}

enum class ApiStatus {LOADING,SUCCESS,FAILED}
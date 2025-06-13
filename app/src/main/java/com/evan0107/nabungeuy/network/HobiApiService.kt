package com.evan0107.nabungeuy.network

import com.evan0107.nabungeuy.model.HobiResponse // Import HobiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://hobi-api.bagasaldianata.my.id/api/"

private const val BASE_IMAGE_URL = "https://hobi-api.bagasaldianata.my.id/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface HobiApiService {
    // Endpoint untuk mendapatkan daftar hobi dari API Laravel
    @GET("hobi-items")
    suspend fun getHobiItems(): HobiResponse
}

object HobiApi {
    val service: HobiApiService by lazy {
        retrofit.create(HobiApiService::class.java)
    }

    // Fungsi untuk mengkonstruksi URL gambar lengkap
    fun getHobiItemImageUrl(imagePath: String?): String? {
        return if (imagePath != null) {

            "$BASE_IMAGE_URL${imagePath.removePrefix("/")}"
        } else {
            null
        }
    }
}

enum class ApiStatus {LOADING,SUCCESS,FAILED}
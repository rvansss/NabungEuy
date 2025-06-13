package com.evan0107.nabungeuy.model

import com.squareup.moshi.Json

data class HobiItem(
    val id: Int,
    val name: String,
    val brand: String,
    val price: String,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String
)
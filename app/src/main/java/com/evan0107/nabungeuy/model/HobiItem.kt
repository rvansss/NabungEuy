package com.evan0107.nabungeuy.model

//import com.google.gson.annotations.SerializedName

data class HobiItem(
    val id: Int,
    val name: String,
    val brand: String,
    val price: String,
    val image_url: String
)
//    @SerializedName("image_url")
//    val imageUrl: String?,
//    @SerializedName("created_at")
//    val createdAt: String?,
//    @SerializedName("updated_at")
//    val updatedAt: String?

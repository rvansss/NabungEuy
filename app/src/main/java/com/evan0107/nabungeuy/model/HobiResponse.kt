package com.evan0107.nabungeuy.model

data class HobiResponse(
    val status: String,
    val message: String,
    val data: List<HobiItem> // Ini adalah daftar HobiItem
)
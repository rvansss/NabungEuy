package com.evan0107.nabungeuy.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citaCita")
data class CitaCita(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val harga: String,
    val gambarUri: Uri? = null
)

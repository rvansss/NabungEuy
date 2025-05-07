package com.evan0107.nabungeuy.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citacita")
data class CitaCita(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val harga: String,
    val gambarUri: Uri?
)

package com.evan0107.nabungeuy

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class SavingItem(val nama: String, val harga: String, val imageUri: Uri?)

class SavingViewModel : ViewModel() {
    val listData = mutableStateListOf<CitaCitaItem>()

    fun tambahData(item: CitaCitaItem) {
        listData.add(item)
    }
}

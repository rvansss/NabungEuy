package com.evan0107.nabungeuy.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.evan0107.nabungeuy.model.CitaCita


class SavingViewModel : ViewModel() {
    val listData = mutableStateListOf<CitaCita>()

    var selectedItem by mutableStateOf<CitaCita?>(null)
        private set

    fun tambahData(item: CitaCita) {
        listData.add(item)
    }

    fun pilihItem(item: CitaCita) {
        selectedItem = item
    }
}

package com.evan0107.nabungeuy.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.evan0107.nabungeuy.model.CitaCita


class SavingViewModel : ViewModel() {
    val listData = mutableStateListOf<CitaCita>()

    fun tambahData(item: CitaCita) {
        listData.add(item)
    }
}

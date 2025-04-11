package com.evan0107.nabungeuy

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel


class SavingViewModel : ViewModel() {
    val listData = mutableStateListOf<CitaCitaItem>()

    fun tambahData(item: CitaCitaItem) {
        listData.add(item)
    }
}

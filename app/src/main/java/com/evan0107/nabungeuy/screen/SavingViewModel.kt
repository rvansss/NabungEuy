package com.evan0107.nabungeuy.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan0107.nabungeuy.database.CitaCitaDao
import com.evan0107.nabungeuy.model.CitaCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SavingViewModel(dao: CitaCitaDao) : ViewModel() {
    val listData: StateFlow<List<CitaCita>> = dao.getCitaCita()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val citaCitaDao = dao // simpan sebagai field opsional

    var selectedItem by mutableStateOf<CitaCita?>(null)
    private set

//    fun getCitaCita(id: Int): CitaCita? {
//        return listData.value.find { it.id == id }
//    }

    fun pilihItem(item: CitaCita) {
        selectedItem = item
    }

    fun tambahData(item: CitaCita) {
        viewModelScope.launch {
            citaCitaDao.insert(item)
        }
    }

}

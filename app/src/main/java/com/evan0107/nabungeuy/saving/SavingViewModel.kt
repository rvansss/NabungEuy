package com.evan0107.nabungeuy.saving

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.HobiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SavingViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<HobiItem>())
        private set

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data.value = HobiApi.service.getHobiItem()
            } catch (e: Exception) {
                Log.d("SavingViewModel", "Failure: ${e.message}")
            }
        }
    }
}




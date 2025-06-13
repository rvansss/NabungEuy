package com.evan0107.nabungeuy.saving

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan0107.nabungeuy.network.HobiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SavingViewModel : ViewModel() {

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = HobiApi.service.getHobiItems()
                Log.d("SavingViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("SavingViewModel", "Failure: ${e.message}")
            }
        }
    }
}




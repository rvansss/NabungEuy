package com.evan0107.nabungeuy.saving

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.ApiStatus
import com.evan0107.nabungeuy.network.HobiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class SavingViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<HobiItem>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = HobiApi.service.getHobiItem()
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("SavingViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}




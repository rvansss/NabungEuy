package com.evan0107.nabungeuy.saving

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan0107.nabungeuy.model.HobiItem // Import HobiItem
import com.evan0107.nabungeuy.network.ApiStatus
import com.evan0107.nabungeuy.network.HobiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow // Import StateFlow
import kotlinx.coroutines.launch

class SavingViewModel : ViewModel() {


    private val _data = mutableStateOf<List<HobiItem>>(emptyList())
    val data: State<List<HobiItem>> = _data


    private val _status = MutableStateFlow(ApiStatus.LOADING)
    val status: StateFlow<ApiStatus> = _status

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = ApiStatus.LOADING
            try {

                val response = HobiApi.service.getHobiItems()
                _data.value = response.data
                _status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("SavingViewModel", "Failure: ${e.message}", e)
                _status.value = ApiStatus.FAILED
            }
        }
    }
}
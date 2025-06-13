//package com.evan0107.nabungeuy.screen
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.evan0107.nabungeuy.network.HobiApi
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class MainViewModel : ViewModel() {
//
//    init {
//        retrieveData()
//    }
//
//    private fun retrieveData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val result = HobiApi.service.getHewan()
//                Log.d("MainViewModel", "Success: $result")
//            } catch (e: Exception) {
//                Log.d("MainViewModel", "Failure: ${e.message}")
//            }
//        }
//    }
//}
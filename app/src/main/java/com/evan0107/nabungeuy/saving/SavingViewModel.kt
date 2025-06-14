package com.evan0107.nabungeuy.saving

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.ApiStatus
import com.evan0107.nabungeuy.network.HobiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import retrofit2.HttpException

class SavingViewModel : ViewModel() {

    private val _data = mutableStateOf<List<HobiItem>>(emptyList())
    val data: State<List<HobiItem>> = _data

    private val _status = MutableStateFlow(ApiStatus.LOADING)
    val status: StateFlow<ApiStatus> = _status

    private val _addHobiStatus = MutableStateFlow(ApiStatus.SUCCESS)
    val addHobiStatus: StateFlow<ApiStatus> = _addHobiStatus

    private val _deleteHobiStatus = MutableStateFlow(ApiStatus.SUCCESS)
    val deleteHobiStatus: StateFlow<ApiStatus> = _deleteHobiStatus

    private val _updateHobiStatus = MutableStateFlow(ApiStatus.SUCCESS)
    val updateHobiStatus: StateFlow<ApiStatus> = _updateHobiStatus


    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SavingViewModel", "retrieveData() called: Fetching latest data.")
            _status.value = ApiStatus.LOADING
            try {
                val response = HobiApi.service.getHobiItems()
                _data.value = response.data
                _status.value = ApiStatus.SUCCESS
                Log.d("SavingViewModel", "retrieveData() success. New data size: ${_data.value.size}")
            } catch (e: Exception) {
                Log.e("SavingViewModel", "Failure to retrieve data in retrieveData(): ${e.message}", e)
                _status.value = ApiStatus.FAILED
            }
        }
    }

    fun addHobi(hobi: HobiItem, imageBitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SavingViewModel", "addHobi() called for: ${hobi.name}")
            // Log data to be sent for debugging
            Log.d("SavingViewModel", "Hobi data for add: Name='${hobi.name}', Brand='${hobi.brand}', Price='${hobi.price}'")

            _addHobiStatus.value = ApiStatus.LOADING
            try {
                val imagePart = imageBitmap?.let { bitmap ->
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val byteArray = stream.toByteArray()
                    val requestBody = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image_url", "hobi_image.png", requestBody) // Match backend field name
                }

                val namePart = hobi.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val brandPart = hobi.brand.toRequestBody("text/plain".toMediaTypeOrNull())
                val pricePart = hobi.price.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = HobiApi.service.addHobi(
                    name = namePart,
                    brand = brandPart,
                    price = pricePart,
                    image = imagePart
                )

                if (response.status == "success") {
                    _addHobiStatus.value = ApiStatus.SUCCESS
                    Log.d("SavingViewModel", "Hobi added successfully. Calling retrieveData().")
                    retrieveData()
                } else {
                    _addHobiStatus.value = ApiStatus.FAILED
                    Log.e("SavingViewModel", "Failed to add hobi: ${response.message}")
                }
            } catch (e: HttpException) { // Catch HttpException specifically
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SavingViewModel", "HTTP Error adding hobi: ${e.code()} - $errorBody", e)
                _addHobiStatus.value = ApiStatus.FAILED
            } catch (e: Exception) {
                Log.e("SavingViewModel", "Error adding hobi: ${e.message}", e)
                _addHobiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun deleteHobi(hobiId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SavingViewModel", "deleteHobi() called for ID: $hobiId")
            _deleteHobiStatus.value = ApiStatus.LOADING
            try {
                val response = HobiApi.service.deleteHobi(hobiId)
                if (response.status == "success") {
                    _deleteHobiStatus.value = ApiStatus.SUCCESS
                    Log.d("SavingViewModel", "Hobi deleted successfully. Calling retrieveData().")
                    retrieveData()
                } else {
                    _deleteHobiStatus.value = ApiStatus.FAILED
                    Log.e("SavingViewModel", "Failed to delete hobi: ${response.message}")
                }
            } catch (e: HttpException) { // Catch HttpException specifically
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SavingViewModel", "HTTP Error deleting hobi: ${e.code()} - $errorBody", e)
                _deleteHobiStatus.value = ApiStatus.FAILED
            } catch (e: Exception) {
                Log.e("SavingViewModel", "Error deleting hobi: ${e.message}", e)
                _deleteHobiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun updateHobi(hobi: HobiItem, imageBitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SavingViewModel", "updateHobi() called for ID: ${hobi.id}, Name: ${hobi.name}")
            // Log data to be sent for debugging
            Log.d("SavingViewModel", "Hobi data for update: Name='${hobi.name}', Brand='${hobi.brand}', Price='${hobi.price}'")

            _updateHobiStatus.value = ApiStatus.LOADING
            try {
                val imagePart: MultipartBody.Part? = imageBitmap?.let { bitmap ->
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val byteArray = stream.toByteArray()
                    val requestBody = byteArray.toRequestBody("image/png".toMediaTypeOrNull())
                    // Penting: Pastikan nama field ini cocok dengan yang di backend (image_url)
                    MultipartBody.Part.createFormData("image_url", "hobi_image.png", requestBody)
                } ?: run {

                    if (hobi.imageUrl != null && hobi.imageUrl.isNotEmpty()) {
                        val emptyRequestBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("image_url", "", emptyRequestBody)
                    } else {
                        null
                    }
                }

                val namePart = hobi.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val methodPart = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())
                val brandPart = hobi.brand.toRequestBody("text/plain".toMediaTypeOrNull())
                val pricePart = hobi.price.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = HobiApi.service.updateHobi(
                    hobiId = hobi.id,
                    method = methodPart,
                    name = namePart,
                    brand = brandPart,
                    price = pricePart,
                    image = imagePart
                )

                if (response.status == "success") {
                    _updateHobiStatus.value = ApiStatus.SUCCESS
                    Log.d("SavingViewModel", "Hobi updated successfully. Calling retrieveData().")
                    retrieveData()
                } else {
                    _updateHobiStatus.value = ApiStatus.FAILED
                    Log.e("SavingViewModel", "Failed to update hobi: ${response.message}")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("SavingViewModel", "HTTP Error updating hobi: ${e.code()} - $errorBody", e)
                _updateHobiStatus.value = ApiStatus.FAILED
            } catch (e: Exception) {
                Log.e("SavingViewModel", "Generic Error updating hobi: ${e.message}", e)
                _updateHobiStatus.value = ApiStatus.FAILED
            }
        }
    }
}
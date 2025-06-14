package com.evan0107.nabungeuy.saving

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.evan0107.nabungeuy.R
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.HobiApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahHobiDialog(
    onDismissRequest: () -> Unit,
    onSaveHobi: (HobiItem, Bitmap?) -> Unit,
    hobiToEdit: HobiItem? = null
) {
    val context = LocalContext.current

    var namaHobi by remember { mutableStateOf(hobiToEdit?.name ?: "") }
    var brandHobi by remember { mutableStateOf(hobiToEdit?.brand ?: "") }
    var hargaHobi by remember { mutableStateOf(hobiToEdit?.price ?: "") }
    var selectedImageBitmap: Bitmap? by remember { mutableStateOf(null) }

    // Jika sedang dalam mode edit dan belum ada gambar baru dipilih,
    // tampilkan gambar lama dari URL jika ada.
    val initialImageUrl = remember(hobiToEdit) { hobiToEdit?.imageUrl }

    var namaError by remember { mutableStateOf(false) }
    var brandError by remember { mutableStateOf(false) }
    var hargaError by remember { mutableStateOf(false) }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        selectedImageBitmap = getCroppedImage(context.contentResolver, result)
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (hobiToEdit == null) "Tambah Hobi Baru" else "Edit Hobi",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = namaHobi,
                    onValueChange = {
                        namaHobi = it
                        namaError = it.trim().isBlank() // Trim di sini
                    },
                    label = { Text("Nama Hobi") },
                    isError = namaError,
                    supportingText = {
                        if (namaError) {
                            Text("Nama tidak boleh kosong")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = brandHobi,
                    onValueChange = {
                        brandHobi = it
                        brandError = it.trim().isBlank() // Trim di sini
                    },
                    label = { Text("Brand") },
                    isError = brandError,
                    supportingText = {
                        if (brandError) {
                            Text("Brand tidak boleh kosong")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = hargaHobi,
                    onValueChange = {
                        hargaHobi = it
                        // Trim di sini sebelum validasi toDoubleOrNull
                        val trimmedPrice = it.trim()
                        hargaError = trimmedPrice.isBlank() || trimmedPrice.toDoubleOrNull() == null || trimmedPrice.toDoubleOrNull()!! <= 0
                    },
                    label = { Text("Harga (contoh: 4500000.00)") },
                    isError = hargaError,
                    supportingText = {
                        if (hargaError) {
                            Text("Harga tidak boleh kosong, harus angka positif")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Pratinjau Gambar dan Tombol Pilih Gambar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val currentImageToDisplay: Any? = when {
                        selectedImageBitmap != null -> selectedImageBitmap // Gambar baru yang dipilih/dipotong
                        initialImageUrl?.isNotEmpty() == true -> HobiApi.getHobiItemImageUrl(initialImageUrl) // Gambar lama dari hobiToEdit
                        else -> R.drawable.placeholder_image // Placeholder default
                    }

                    if (currentImageToDisplay is Bitmap) {
                        Image(
                            bitmap = currentImageToDisplay.asImageBitmap(),
                            contentDescription = "Gambar Hobi",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentImageToDisplay)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Pilih Gambar",
                            placeholder = painterResource(id = R.drawable.placeholder_image),
                            error = painterResource(id = R.drawable.broken_img),
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val options = CropImageContractOptions(
                                        null, CropImageOptions(
                                            imageSourceIncludeGallery = true,
                                            imageSourceIncludeCamera = true,
                                            fixAspectRatio = true
                                        )
                                    )
                                    imageCropLauncher.launch(options)
                                },
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = {
                        val options = CropImageContractOptions(
                            null, CropImageOptions(
                                imageSourceIncludeGallery = true,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            )
                        )
                        imageCropLauncher.launch(options)
                    }) {
                        Text("Pilih Gambar / Ambil Foto")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Simpan dan Batal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(onClick = onDismissRequest) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            // Lakukan validasi sebelum menyimpan
                            namaError = namaHobi.trim().isBlank()
                            brandError = brandHobi.trim().isBlank()
                            val trimmedHarga = hargaHobi.trim()
                            hargaError = trimmedHarga.isBlank() || trimmedHarga.toDoubleOrNull() == null || trimmedHarga.toDoubleOrNull()!! <= 0

                            if (!namaError && !brandError && !hargaError) {
                                // Buat objek HobiItem, gunakan ID yang ada jika mode edit
                                val hobiToSave = HobiItem(
                                    id = hobiToEdit?.id ?: 0, // Gunakan ID yang ada atau 0 jika baru
                                    name = namaHobi.trim(), // Trim final sebelum dikirim
                                    brand = brandHobi.trim(), // Trim final sebelum dikirim
                                    price = hargaHobi.trim(), // Trim final sebelum dikirim
                                    imageUrl = hobiToEdit?.imageUrl, // Pertahankan URL lama untuk keperluan data HobiItem di klien
                                    createdAt = hobiToEdit?.createdAt ?: "",
                                    updatedAt = ""
                                )
                                onSaveHobi(hobiToSave, selectedImageBitmap)
                            }
                        },
                        // Tombol aktif jika semua valid (gunakan variabel error yang sudah di-trim)
                        enabled = !namaError && !brandError && !hargaError
                    ) {
                        Text(if (hobiToEdit == null) "Simpan" else "Update")
                    }
                }
            }
        }
    }
}

// Fungsi getCroppedImage, ditempatkan di sini karena digunakan oleh TambahHobiDialog
private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error cropping image: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: run {
        Log.e("IMAGE", "Cropped image URI is null.")
        return null
    }

    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(resolver, uri)
        }
    } catch (e: Exception) {
        Log.e("IMAGE", "Error decoding cropped image bitmap: ${e.message}", e)
        null
    }
}
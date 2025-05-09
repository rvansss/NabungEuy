package com.evan0107.nabungeuy.saving

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.evan0107.nabungeuy.model.CitaCita

@Composable
fun FormInputScreen(
    navController: NavController,
    viewModel: SavingViewModel,
    itemId: Int?
) {
    var nama by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Ambil data jika itemId ada
    LaunchedEffect(itemId) {
        if (itemId != null) {
            val item = viewModel.getCitaCita(itemId)
            item?.let {
                nama = it.nama
                harga = it.harga
                imageUri = it.gambarUri
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            if (itemId != null) "Edit Tabungan" else "Tambah Tabungan",
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = {
                if (nama.isNotBlank() && harga.isNotBlank()) {
                    val item = CitaCita(
                        id = itemId ?: 0, // id 0 untuk item baru
                        nama = nama,
                        harga = harga,
                        gambarUri = imageUri
                    )
                    viewModel.tambahData(item) // tetap gunakan insert(REPLACE)

                    // Reset form dan kembali
                    nama = ""
                    harga = ""
                    imageUri = null
                    navController.navigate("saving")
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (itemId != null) "Update" else "Simpan")
        }

        TextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama Barang") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = harga,
            onValueChange = { harga = it },
            label = { Text("Harga") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pilih Gambar")
        }

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}



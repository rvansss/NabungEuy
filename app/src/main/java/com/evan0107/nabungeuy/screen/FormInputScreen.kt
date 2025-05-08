package com.evan0107.nabungeuy.screen

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
import coil.compose.rememberAsyncImagePainter
import com.evan0107.nabungeuy.model.CitaCita

@Composable
fun FormInputScreen(viewModel: SavingViewModel) {
    var nama by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

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
        Text("Tambah Tabungan", fontWeight = FontWeight.Bold)

        Button(
            onClick = {
                if (nama.isNotBlank() && harga.isNotBlank()) {
                    val item = CitaCita(
                        nama = nama,
                        harga = harga,
                        gambarUri = imageUri)
                    viewModel.tambahData(item)

                    // Reset form setelah simpan
                    nama = ""
                    harga = ""
                    imageUri = null
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Simpan")
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


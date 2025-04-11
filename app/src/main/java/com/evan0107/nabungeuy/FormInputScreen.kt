package com.evan0107.nabungeuy

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormInputScreen() {
    var nama by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Tambah Cita-Cita", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama Barang") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = harga,
            onValueChange = { harga = it },
            label = { Text("Harga") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Simpan logika simpan data di sini
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Simpan")
        }
    }
}

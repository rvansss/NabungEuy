package com.evan0107.nabungeuy.saving

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.evan0107.nabungeuy.R

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: SavingViewModel
) {
    val item = viewModel.selectedItem

    if (item == null) {
        Text("Tidak ada data dipilih.")
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Konten utama di tengah
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val painter = item.gambarUri?.let { rememberAsyncImagePainter(it) }
                ?: painterResource(id = R.drawable.ic_launcher_foreground)

            Image(
                painter = painter,
                contentDescription = item.nama,
                modifier = Modifier.size(128.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Nama: ${item.nama}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Harga: ${item.harga}", style = MaterialTheme.typography.bodyLarge)
        }

        // Tombol Hapus dan Edit di bawah tengah
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                viewModel.hapusData(item)
                navController.popBackStack()
            }) {
                Text("Hapus")
            }

            Button(onClick = {
                navController.navigate("form_input/${item.id}") // Bawa ID untuk edit
            }) {
                Text("Edit")
            }
        }
    }
}

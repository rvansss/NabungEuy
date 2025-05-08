package com.evan0107.nabungeuy.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil.compose.rememberAsyncImagePainter
import com.evan0107.nabungeuy.R


@Composable
fun SavingScreen(navController: NavController, viewModel: SavingViewModel) {
    val citaCitaList by viewModel.listData.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Van’s Cita-cita",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (citaCitaList.isEmpty()) {
                Text(
                    text = "Belum ada data,\nsilahkan tambah cita cita muuu",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(citaCitaList) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable {
                                    viewModel.pilihItem(item)
                                    navController.navigate("detail_screen")
                                },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                val painter = item.gambarUri?.let { rememberAsyncImagePainter(it) }
                                    ?: painterResource(id = R.drawable.ic_launcher_foreground)

                                Image(
                                    painter = painter,
                                    contentDescription = item.nama,
                                    modifier = Modifier.size(64.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = item.nama, fontWeight = FontWeight.Bold)
                                Text(text = item.harga)
                            }
                        }
                    }
                }
            }
        }

        // Tombol Tambah Data
        FloatingActionButton(
            onClick = { navController.navigate("form_input") },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Create, contentDescription = "Tambah")
        }
    }
}


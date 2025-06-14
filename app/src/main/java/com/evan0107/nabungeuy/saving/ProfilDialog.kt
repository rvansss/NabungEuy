package com.evan0107.nabungeuy.saving // Periksa apakah package ini benar atau seharusnya com.evan0107.nabungeuy.screen

import androidx.compose.foundation.Image // Perlu ini jika tidak pakai Coil untuk placeholder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape // Untuk gambar profil yang berbentuk lingkaran
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button // Tambahkan import Button untuk tombol konfirmasi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Untuk clip CircleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.evan0107.nabungeuy.R
import com.evan0107.nabungeuy.model.User // Pastikan model.User diimport dengan benar

@Composable
fun ProfilDialog(
    user: User,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit // Ini adalah aksi untuk konfirmasi (logout)
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth() // Buat card mengisi lebar
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), // Column mengisi lebar card
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Pusatkan konten vertikal
            ) {
                // Gambar Profil (menggunakan AsyncImage dari Coil)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl) // Menggunakan URL foto profil dari user
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.profil), // Deskripsi konten gambar
                    contentScale = ContentScale.Crop,
                    // Placeholder dan error drawable
                    placeholder = painterResource(id = R.drawable.loading_img), // Pastikan drawable ini ada
                    error = painterResource(id = R.drawable.broken_img), // Pastikan drawable ini ada
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape) // Membuat gambar berbentuk lingkaran
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nama Pengguna
                Text(
                    text = if (user.name.isNotEmpty()) user.name else "Nama Tidak Tersedia", // Tampilkan nama asli atau fallback
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Email Pengguna
                Text(
                    text = if (user.email.isNotEmpty()) user.email else "Email Tidak Tersedia", // Tampilkan email asli atau fallback
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Warna yang sedikit redup
                )

                Spacer(modifier = Modifier.height(24.dp)) // Spacer sebelum tombol

                // Baris Tombol Aksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround // Tombol diatur di tengah dengan spasi di antaranya
                ) {
                    // Tombol "Tutup" (Dismiss)
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp) // Berikan bobot dan padding
                    ) {
                        Text(stringResource(R.string.tutup)) // Asumsi R.string.tutup ada
                    }

                    // Tombol "Logout" (Konfirmasi)
                    Button( // Menggunakan Button karena ini adalah aksi utama
                        onClick = { onConfirmation() }, // Panggil aksi konfirmasi
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error) // Warna merah untuk logout
                    ) {
                        Text(stringResource(R.string.logout)) // Asumsi R.string.logout ada
                    }
                }
            }
        }
    }
}
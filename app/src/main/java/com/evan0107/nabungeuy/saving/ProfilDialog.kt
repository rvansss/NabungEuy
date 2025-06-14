package com.evan0107.nabungeuy.saving // Periksa apakah package ini benar atau seharusnya com.evan0107.nabungeuy.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.evan0107.nabungeuy.model.User

@Composable
fun ProfilDialog(
    user: User,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.profil),
                    contentScale = ContentScale.Crop,
                    // Placeholder dan error drawable
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = if (user.name.isNotEmpty()) user.name else "Nama Tidak Tersedia", // Tampilkan nama asli atau fallback
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )


                Text(
                    text = if (user.email.isNotEmpty()) user.email else "Email Tidak Tersedia", // Tampilkan email asli atau fallback
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    ) {
                        Text(stringResource(R.string.tutup))
                    }


                    Button(
                        onClick = { onConfirmation() },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(stringResource(R.string.logout))
                    }
                }
            }
        }
    }
}
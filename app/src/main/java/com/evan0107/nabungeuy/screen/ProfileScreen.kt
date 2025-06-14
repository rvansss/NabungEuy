package com.evan0107.nabungeuy.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.evan0107.nabungeuy.BuildConfig
import com.evan0107.nabungeuy.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.exceptions.ClearCredentialException
import com.evan0107.nabungeuy.model.User
import com.evan0107.nabungeuy.network.UserDataStore
import androidx.compose.foundation.BorderStroke // Import BorderStroke untuk OutlinedButton
import androidx.compose.ui.res.stringResource
import com.evan0107.nabungeuy.saving.ProfilDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    var showDialog by remember { mutableStateOf(false) } // State untuk mengontrol visibilitas dialog

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            // Jika belum login, lakukan Sign In
                            coroutineScope.launch {
                                signIn(context, dataStore)
                            }
                        } else {
                            // Jika sudah login, tampilkan dialog konfirmasi logout
                            showDialog = true
                        }
                    }) {
                        // Ubah ikon berdasarkan status login
                        val iconRes = if (user.email.isEmpty()) R.drawable.account_circle_24 else R.drawable.logout_24 // Asumsi ada ic_logout drawable
                        val tintColor = if (user.email.isEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = stringResource(R.string.profil), // Konten deskripsi tetap sama
                            tint = tintColor
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Terapkan padding dari Scaffold
                .padding(24.dp), // Padding tambahan untuk konten
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Gambar profil
            Image(
                // Menggunakan placeholder untuk foto profil. Jika Anda ingin memuat dari URL, gunakan Coil/Glide.
                // Contoh dengan Coil (pastikan Anda telah menambahkan dependency coil-compose):
                // painter = rememberAsyncImagePainter(model = user.photoUrl),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Foto Profil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama Pengguna
            Text(
                text = if (user.name.isNotEmpty()) user.name else "Evansius Rafael S",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // Email Pengguna (tampilkan jika ada)
            if (user.email.isNotEmpty()) {
                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Cita-cita
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cita-citaku:\nMemiliki aksesoris audio adalah hal yang membuat saya bahagia.",
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Deskripsi aplikasi
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Aplikasi ini adalah aplikasi pengelolaan tabungan sederhana.",
                    modifier = Modifier.padding(12.dp)
                )
            }
            // Tombol Log Out (Hanya tampilkan jika pengguna sudah login dan jika Anda ingin tombol terpisah dari icon di TopAppBar)
            // Jika Anda hanya ingin logout via icon TopAppBar, Anda bisa menghapus bagian ini.
            /*
            if (user.email.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            signOut(context, dataStore)
                        }
                    },
                    modifier = Modifier.padding(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text(
                        text = stringResource(R.string.logout),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            */
        }
    }

    // Bagian Dialog diletakkan di sini, setelah Scaffold
    if (showDialog) {
        ProfilDialog(
            user = user, // Teruskan data user ke dialog
            onDismissRequest = { showDialog = false }, // Ketika dialog diminta tutup
            onConfirmation = { // Ketika tombol konfirmasi di dialog diklik
                coroutineScope.launch {
                    signOut(context, dataStore)
                }
                showDialog = false // Tutup dialog setelah aksi
            }
        )
    }
}


private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
            Log.d("SIGN-IN", "User signed in: $nama ($email)")
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error parsing Google ID token: ${e.message}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
        Log.d("SIGN-OUT", "User signed out successfully.")
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-OUT", "Error during sign out: ${e.errorMessage}")
    }
}

// Pastikan Anda memiliki definisi ProfilDialog di file yang sama atau import jika di file lain
// Contoh sederhana ProfilDialog (Anda mungkin perlu mengisi kontennya sesuai desain Anda)
/*
@Composable
fun ProfilDialog(
    user: User,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Konfirmasi Logout") },
        text = { Text("Anda yakin ingin logout dari akun ${user.email}?") },
        confirmButton = {
            Button(onClick = onConfirmation) {
                Text(stringResource(R.string.logout)) // Asumsi R.string.logout ada
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.tutup)) // Asumsi R.string.tutup ada
            }
        }
    )
}
*/
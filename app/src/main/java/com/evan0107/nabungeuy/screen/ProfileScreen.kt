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
import androidx.compose.ui.res.stringResource
import com.evan0107.nabungeuy.saving.ProfilDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    var showDialog by remember { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {

                            coroutineScope.launch {
                                signIn(context, dataStore)
                            }
                        } else {

                            showDialog = true
                        }
                    }) {

                        val iconRes = if (user.email.isEmpty()) R.drawable.account_circle_24 else R.drawable.logout_24
                        val tintColor = if (user.email.isEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = stringResource(R.string.profil),
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
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Image(

                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Foto Profil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = if (user.name.isNotEmpty()) user.name else "Evansius Rafael S",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )


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
        }
    }

    if (showDialog) {
        ProfilDialog(
            user = user,
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                coroutineScope.launch {
                    signOut(context, dataStore)
                }
                showDialog = false
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


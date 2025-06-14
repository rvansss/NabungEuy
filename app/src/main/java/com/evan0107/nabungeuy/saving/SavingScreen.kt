package com.evan0107.nabungeuy.saving

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.evan0107.nabungeuy.R
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.ApiStatus
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import com.evan0107.nabungeuy.network.HobiApi
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingScreen(
    modifier:Modifier   = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: SavingViewModel = viewModel()
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    var showAddHobiDialog by remember { mutableStateOf(false) }
    var hobiToEdit by remember { mutableStateOf<HobiItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabungan Hobi") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                hobiToEdit = null
                showAddHobiDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.tambah_hobi)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        when (status) {
            ApiStatus.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            ApiStatus.SUCCESS -> {
                LazyVerticalGrid(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(data) { hobiItem ->
                        ListItem(
                            hobiItem = hobiItem,
                            onDeleteClick = { hobiId ->
                                viewModel.deleteHobi(hobiId)
                            },
                            onEditClick = { hobiToEditItem ->
                                hobiToEdit = hobiToEditItem
                                showAddHobiDialog = true
                            }
                        )
                    }
                }
            }
            ApiStatus.FAILED -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.error))
                    Button(
                        onClick = { viewModel.retrieveData() },
                        modifier = Modifier.padding(top = 16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.try_again))
                    }
                }
            }
        }
    }

    if (showAddHobiDialog) {
        TambahHobiDialog(
            onDismissRequest = {
                showAddHobiDialog = false
                hobiToEdit = null
            },
            onSaveHobi = { hobi, bitmap ->
                if (hobi.id == 0) {
                    viewModel.addHobi(hobi, bitmap)
                } else {
                    viewModel.updateHobi(hobi, bitmap)
                }
                showAddHobiDialog = false
                hobiToEdit = null
            },
            hobiToEdit = hobiToEdit
        )
    }

    val addHobiStatus by viewModel.addHobiStatus.collectAsState()
    val deleteHobiStatus by viewModel.deleteHobiStatus.collectAsState()
    val updateHobiStatus by viewModel.updateHobiStatus.collectAsState()

    if (addHobiStatus == ApiStatus.LOADING || deleteHobiStatus == ApiStatus.LOADING || updateHobiStatus == ApiStatus.LOADING) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (addHobiStatus == ApiStatus.FAILED || deleteHobiStatus == ApiStatus.FAILED || updateHobiStatus == ApiStatus.FAILED) {
        Log.e("SavingScreen", "Operasi Gagal. Cek Logcat untuk detail.")
    }
}

@Composable
fun ListItem(hobiItem: HobiItem, onDeleteClick: (Int) -> Unit, onEditClick: (HobiItem) -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        val fullImageUrl = HobiApi.getHobiItemImageUrl(hobiItem.imageUrl)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(fullImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.gambar, hobiItem.name),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.broken_img),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(8.dp)
        ){
            Text(
                text = hobiItem.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = hobiItem.brand,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Rp ${hobiItem.price}",
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                IconButton(onClick = { onEditClick(hobiItem) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Hobi",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Hobi",
                        tint = Color.Red
                    )
                }
            }
        }
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Hobi") },
            text = { Text("Anda yakin ingin menghapus '${hobiItem.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick(hobiItem.id)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}


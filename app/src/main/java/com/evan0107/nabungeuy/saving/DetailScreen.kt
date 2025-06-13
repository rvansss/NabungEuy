//package com.evan0107.nabungeuy.saving
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.evan0107.nabungeuy.R
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.runtime.rememberCoroutineScope
//import kotlinx.coroutines.launch
//
//@Composable
//fun DetailScreen(
//    navController: NavController,
//    viewModel: SavingViewModel
//) {
//    val item = viewModel.selectedItem
//    val showDialog = remember { mutableStateOf(false) }
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    if (item == null) {
//        Text("Tidak ada data dipilih.")
//        return
//    }
//
//    if (showDialog.value) {
//        AlertDialog(
//            onDismissRequest = { showDialog.value = false },
//            title = { Text("Konfirmasi Hapus") },
//            text = { Text("Apakah kamu yakin ingin menghapus data ini?") },
//            confirmButton = {
//                TextButton(onClick = {
//                    viewModel.hapusData(item)
//                    showDialog.value = false
//                    coroutineScope.launch {
//                        snackbarHostState.showSnackbar("Data berhasil dihapus")
//                    }
//                    navController.popBackStack()
//                }) {
//                    Text("Hapus")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDialog.value = false }) {
//                    Text("Batal")
//                }
//            }
//        )
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
//    ) { paddingValues ->
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .padding(paddingValues)) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                val painter = item.gambarUri?.let { rememberAsyncImagePainter(it) }
//                    ?: painterResource(id = R.drawable.ic_launcher_foreground)
//
//                Image(
//                    painter = painter,
//                    contentDescription = item.nama,
//                    modifier = Modifier.size(128.dp)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Text("Nama: ${item.nama}", style = MaterialTheme.typography.titleLarge)
//                Spacer(modifier = Modifier.height(8.dp))
//                Text("Harga: ${item.harga}", style = MaterialTheme.typography.bodyLarge)
//            }
//
//            Row(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Button(onClick = {
//                    showDialog.value = true
//                }) {
//                    Text("Hapus")
//                }
//
//                Button(onClick = {
//                    navController.navigate("form_input/${item.id}")
//                }) {
//                    Text("Edit")
//                }
//            }
//        }
//    }
//}
//

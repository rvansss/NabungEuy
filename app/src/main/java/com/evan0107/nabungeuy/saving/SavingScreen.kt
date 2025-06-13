package com.evan0107.nabungeuy.saving

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.evan0107.nabungeuy.R
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.HobiApi



@Composable
fun SavingScreen(
    modifier:Modifier   = Modifier,
    navController: NavController

) {
    val viewModel: SavingViewModel = viewModel()
    val data by viewModel.data

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize().padding(4.dp),
        columns = GridCells.Fixed(2),
    ) {
        items(data) { ListItem(hobiItem = it) }
    }
}

@Composable
fun ListItem(hobiItem: HobiItem) {
    Box(
        modifier = Modifier.padding(4.dp).border(1.dp, Color.Gray)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(HobiApi.getHobiItemUrl(hobiItem.image_url))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.gambar, hobiItem.name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
    }
}
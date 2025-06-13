package com.evan0107.nabungeuy.saving

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.evan0107.nabungeuy.R
import com.evan0107.nabungeuy.model.HobiItem
import com.evan0107.nabungeuy.network.ApiStatus
import com.evan0107.nabungeuy.network.HobiApi



@Composable
fun SavingScreen(
    modifier:Modifier   = Modifier,
    navController: NavController
        ) {
    val viewModel: SavingViewModel = viewModel()
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiStatus.SUCCESS -> {

        }
    }


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
        modifier = Modifier.padding(4.dp).border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(HobiApi.getHobiItemUrl(hobiItem.image_url))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.gambar, hobiItem.name),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.broken_img),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
        Column (
            modifier = Modifier.fillMaxWidth().padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(4.dp)
        ){
            Text(
                text = hobiItem.name,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = hobiItem.brand,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
//            Text(
//                text = hobiItem.price,
//                fontWeight = FontStyle.Italic,
//                fontSize = 14.sp,
//                color = Color.White
//
//            )

        }
    }
}
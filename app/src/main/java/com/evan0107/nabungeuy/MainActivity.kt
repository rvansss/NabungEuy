package com.evan0107.nabungeuy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evan0107.nabungeuy.ui.theme.NabungEuyTheme

class MainActivity : ComponentActivity() {
    private var isDarkMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NabungEuyTheme(darkTheme = isDarkMode) {
                Scaffold(
                    topBar = {
                        TopBar(
                            isDarkMode = isDarkMode,
                            onToggleTheme = { isDarkMode = !isDarkMode }
                        )
                    }
                ) { innerPadding ->
                    // Konten nanti di sini
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Text("Isi nanti di sini")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(isDarkMode: Boolean, onToggleTheme: () -> Unit) {
    TopAppBar(
//        backgroundColor = Color(0xFFFFF0F0),
//        elevation = 2.dp,
        title = {
            Text("Van’s Tabungan", fontWeight = FontWeight.Bold)
        },
        actions = {
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleTheme() }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFF0F0)
        )
    )
}


@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    // Gunakan remember agar bisa di-toggle di Compose Preview interaktif (kalau enabled)
    val isDarkMode = remember { mutableStateOf(false) }

    NabungEuyTheme {
        TopBar(
            isDarkMode = isDarkMode.value,
            onToggleTheme = { isDarkMode.value = !isDarkMode.value }
        )
    }
}
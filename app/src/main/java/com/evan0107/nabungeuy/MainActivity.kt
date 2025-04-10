package com.evan0107.nabungeuy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var isDarkMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NabungEuyTheme(darkTheme = isDarkMode) {
                Scaffold(
                    topBar =
                    {
                        TopBar(
                            isDarkMode = isDarkMode,
                            onToggleTheme = { isDarkMode = !isDarkMode }
                        )
                    },
                    bottomBar =
                    {
                        BottomNavbar()
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        MainPerhitungan()
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

@Composable
fun MainPerhitungan() {

    var danaTerkumpul by remember { mutableStateOf("") }
    var danaKebutuhan by remember { mutableStateOf("") }
    var danaSisa by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Danakuu", fontWeight = FontWeight.SemiBold)

        Text("Dana Terkumpul", fontWeight = FontWeight.SemiBold)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = danaTerkumpul,
            onValueChange = { danaTerkumpul = it },
            label = { Text("Terkumpul") },
            singleLine = true
        )

        Text("Dana Kebutuhan", fontWeight = FontWeight.SemiBold)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = danaKebutuhan,
            onValueChange = { danaKebutuhan = it },
            label = { Text("Kebutuhan") },
            singleLine = true
        )

        Text("Dana Sisa", fontWeight = FontWeight.SemiBold)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = danaSisa,
            onValueChange = {},
            label = { Text("Dana Sisa") },
            readOnly = true,
            singleLine = true
        )

//        Spacer(modifier = Modifier.weight(1f)) // Dorong tombol ke bawah

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    val terkumpul = danaTerkumpul.toFloatOrNull() ?: 0f
                    val kebutuhan = danaKebutuhan.toFloatOrNull() ?: 0f
                    val sisa = terkumpul - kebutuhan

                    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID")) // Indonesia
                    danaSisa = formatter.format(sisa)
                }
            ) {
                Text("Hitung")
            }
        }
    }
}

@Composable
fun BottomNavbar() {
    var selectedIndex by remember { mutableStateOf(0) }

    NavigationBar(
        containerColor = Color(0xFFFFF0F0),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Gallery") },
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Done, contentDescription = "Saving") },
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 }
        )
    }
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
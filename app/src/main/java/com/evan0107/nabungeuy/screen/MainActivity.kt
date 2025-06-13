package com.evan0107.nabungeuy.screen

//import SavingViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.evan0107.nabungeuy.ui.theme.NabungEuyTheme
import java.text.NumberFormat
import java.util.Locale
import com.evan0107.nabungeuy.database.CitaCitaDb
//import com.evan0107.nabungeuy.saving.DetailScreen
//import com.evan0107.nabungeuy.saving.FormInputScreen
import com.evan0107.nabungeuy.saving.SavingScreen
import com.evan0107.nabungeuy.saving.SavingViewModel
import com.evan0107.nabungeuy.ui.theme.AppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val db = CitaCitaDb.getInstance(applicationContext)
//            val viewModel: SavingViewModel by viewModels {
//                SavingViewModelFactory(db.dao)
//            }

            var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }

            NabungEuyTheme(theme = currentTheme) {
                AppNavigation(
                    currentTheme = currentTheme,
                    onThemeChange = { currentTheme = it },
                    viewModel = SavingViewModel()
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentTheme: AppTheme, onThemeChange: (AppTheme) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Van’s Tabungan", fontWeight = FontWeight.Bold) },
        actions = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Pilih Tema",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    AppTheme.values().forEach { theme ->
                        DropdownMenuItem(
                            text = { Text(theme.name) },
                            onClick = {
                                onThemeChange(theme)
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Text("Dana Kebutuhan", fontWeight = FontWeight.SemiBold)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = danaKebutuhan,
            onValueChange = { danaKebutuhan = it },
            label = { Text("Kebutuhan") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Text("Dana Sisa", fontWeight = FontWeight.SemiBold)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = danaSisa,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    val terkumpul = danaTerkumpul.toFloatOrNull() ?: 0f
                    val kebutuhan = danaKebutuhan.toFloatOrNull() ?: 0f
                    if (terkumpul < kebutuhan) {
                        danaSisa = "Dana terkumpul tidak cukup"
                    } else {
                        val sisa = terkumpul - kebutuhan
                        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                        danaSisa = formatter.format(sisa)
                    }
                }
            ) {
                Text("Hitung")
            }
        }
    }
}


@Composable
fun BottomNavbar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary, // Ganti dengan warna tema
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Done, contentDescription = "Saving") },
            selected = currentRoute == "saving",
            onClick = {
                if (currentRoute != "saving") {
                    navController.navigate("saving") {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo("home")
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}



@Composable
fun AppNavigation(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    viewModel: SavingViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange
            )
        },
        bottomBar = {
            BottomNavbar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("home") {
                MainPerhitungan()
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("saving") {
                SavingScreen(navController, java.lang.reflect.Modifier())
            }
//            composable("form_input") {
//                FormInputScreen(
//                    navController = navController,
//                    viewModel = viewModel,
//                    itemId = null
//                )
//            }
//
//            composable("detail_screen") {
//                DetailScreen(
//                    viewModel = viewModel,
//                    navController = navController
//                    )
//            }
//            composable("form_input/{id}") { backStackEntry ->
//                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
//                FormInputScreen(
//                    navController = navController,
//                    viewModel = viewModel,
//                    itemId = id
//                )
//            }

        }
    }


}
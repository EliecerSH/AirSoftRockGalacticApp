package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class Product(val name: String, val description: String, val price: Double)

val sampleProducts = listOf(
    Product("M4A1", "Rifle de asalto eléctrico", 250.00),
    Product("AK-47", "Rifle de asalto de gas", 350.00),
    Product("Glock 17", "Pistola de CO2", 120.00),
    Product("MP5", "Subfusil eléctrico", 220.00),
    Product("AWP", "Rifle de francotirador de muelle", 450.00)
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(email: String?, navController: NavController) {
    val context = LocalContext.current
    val dbHelper = UserDbHelper(context)
    var userName by remember { mutableStateOf("") }

    // Get user name from database
    LaunchedEffect(email) {
        if (email != null) {
            val db = dbHelper.readableDatabase
            val projection = arrayOf(UserContract.UserEntry.COLUMN_NAME_NAME)
            val selection = "${UserContract.UserEntry.COLUMN_NAME_EMAIL} = ?"
            val selectionArgs = arrayOf(email)
            val cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )
            with(cursor) {
                if (moveToFirst()) {
                    userName = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_NAME))
                }
                close()
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Bienvenido, $userName", style = MaterialTheme.typography.titleLarge)
                }
                Divider()
                NavigationDrawerItem(label = { Text("Armas") }, selected = false, onClick = { scope.launch { drawerState.close() } })
                NavigationDrawerItem(label = { Text("Vestimentas") }, selected = false, onClick = { scope.launch { drawerState.close() } })
                NavigationDrawerItem(label = { Text("Munición") }, selected = false, onClick = { scope.launch { drawerState.close() } })
                Divider()
                NavigationDrawerItem(label = { Text("Contacto") }, selected = false, onClick = { scope.launch { drawerState.close() } })
                NavigationDrawerItem(label = { Text("Nosotros") }, selected = false, onClick = { scope.launch { drawerState.close() } })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AirSoft Rock Galactic") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(contentPadding = innerPadding) {
                item {
                    Text("Productos", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(start = 16.dp, top= 16.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(sampleProducts) { product ->
                    ProductCard(product, modifier = Modifier.padding(horizontal=16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text(product.description)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text("$${product.price}", fontWeight = FontWeight.Bold)
            }
        }
    }
}
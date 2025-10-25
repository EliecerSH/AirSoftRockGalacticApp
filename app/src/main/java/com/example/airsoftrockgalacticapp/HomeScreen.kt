package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

data class Product(val name: String, val description: String, val price: Double)

val sampleProducts = listOf(
    Product("M4A1", "Rifle de asalto eléctrico", 250.00),
    Product("AK-47", "Rifle de asalto de gas", 350.00),
    Product("Glock 17", "Pistola de CO2", 120.00),
    Product("MP5", "Subfusil eléctrico", 220.00),
    Product("AWP", "Rifle de francotirador de muelle", 450.00)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(email: String?, navController: NavController) {
    val context = LocalContext.current
    val dbHelper = UserDbHelper(context)
    var userName by remember { mutableStateOf("") }

    LaunchedEffect(email) {
        if (email != null) {
            val db = dbHelper.readableDatabase
            val projection = arrayOf(UserContract.UserEntry.COLUMN_NAME_NAME)
            val selection = "${UserContract.UserEntry.COLUMN_NAME_EMAIL} = ?"
            val selectionArgs = arrayOf(email)
            val cursor = db.query(
                UserContract.UserEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null
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
    val bottomNavController = rememberNavController()

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
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = true, // This should be dynamic based on the current route
                        onClick = { bottomNavController.navigate("products") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                        label = { Text("Carrito") },
                        selected = false, // This should be dynamic based on the current route
                        onClick = { bottomNavController.navigate("cart") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                        label = { Text("Cuenta") },
                        selected = false, // This should be dynamic based on the current route
                        onClick = { bottomNavController.navigate("account") }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(navController = bottomNavController, startDestination = "products", modifier = Modifier.padding(innerPadding)) {
                composable("products") { ProductListScreen() }
                composable("cart") { CartScreen() }
                composable("account") { AccountScreen() }
            }
        }
    }
}

@Composable
fun ProductListScreen() {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        item {
            Text("Productos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(sampleProducts) { product ->
            ProductCard(product)
            Spacer(modifier = Modifier.height(8.dp))
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
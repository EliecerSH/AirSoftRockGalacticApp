package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import android.provider.BaseColumns
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class Product(
    val id: Long,
    val slug: String,
    val nombre: String,
    val precio: Double,
    val img: String,
    val cantidad: Int,
    val tipo: String,
    val desc: String
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
                NavigationDrawerItem(label = { Text("Armas") }, selected = false, onClick = { 
                    scope.launch { drawerState.close() } 
                    bottomNavController.navigate("weapons")
                })
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
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == "showcase",
                        onClick = { bottomNavController.navigate("showcase") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                        label = { Text("Carrito") },
                        selected = currentRoute == "cart",
                        onClick = { bottomNavController.navigate("cart") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                        label = { Text("Cuenta") },
                        selected = currentRoute == "account",
                        onClick = { bottomNavController.navigate("account") }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(navController = bottomNavController, startDestination = "showcase", modifier = Modifier.padding(innerPadding)) {
                composable("showcase") { ShowcaseScreen() }
                composable("weapons") { WeaponsScreen() }
                composable("cart") { CartScreen() }
                composable("account") { AccountScreen() }
            }
        }
    }
}

@Composable
fun ShowcaseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido a AirSoft Rock Galactic", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Explora nuestras categorías en el menú lateral.")
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.precio)

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = product.nombre,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(product.nombre, style = MaterialTheme.typography.titleMedium)
                Text(product.desc, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(formattedPrice, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
    }
}
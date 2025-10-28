package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import android.provider.BaseColumns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

val sampleProducts = listOf(
    Product(
        id = 1,
        slug = "mp7-a1",
        nombre = "MP7-A1",
        precio = 349990.0,
        img = "mp7_a1",
        cantidad = 1,
        tipo = "subfusil",
        desc = "Réplica de subfusil MP7-A1 eléctrica, ideal para CQB."
    ),
    Product(
        id = 2,
        slug = "cm16-raider",
        nombre = "CM16 Raider",
        precio = 249990.0,
        img = "cm16_raider",
        cantidad = 1,
        tipo = "fusil",
        desc = "Fusil de asalto CM16 Raider, versátil y fiable."
    ),
    Product(
        id = 3,
        slug = "m9-gbb",
        nombre = "M9 GBB",
        precio = 179990.0,
        img = "m9_gbb",
        cantidad = 1,
        tipo = "pistola",
        desc = "Pistola M9 con sistema Gas Blowback para mayor realismo."
    ),
    Product(
        id = 4,
        slug = "chaleco-tactico",
        nombre = "Chaleco Táctico",
        precio = 89990.0,
        img = "chaleco_tactico",
        cantidad = 1,
        tipo = "equipamiento",
        desc = "Chaleco táctico con sistema MOLLE para personalización."
    )
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
                Column(Modifier.fillMaxHeight().padding(8.dp)) {
                    Text(
                        "👋 Hola, $userName",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Divider()
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Inicio") },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ShoppingCart, null) },
                        label = { Text("Carrito") },
                        selected = false,
                        onClick = { bottomNavController.navigate("cart") }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Cuenta") },
                        selected = false,
                        onClick = { bottomNavController.navigate("account") }
                    )
                    Spacer(Modifier.weight(1f))
                    Divider()
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ExitToApp, null) },
                        label = { Text("Cerrar Sesión") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "AirSoft Rock Galactic",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            bottomBar = {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Inicio") },
                        selected = currentRoute == "showcase",
                        onClick = { bottomNavController.navigate("showcase") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, null) },
                        label = { Text("Carrito") },
                        selected = currentRoute == "cart",
                        onClick = { bottomNavController.navigate("cart") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Cuenta") },
                        selected = currentRoute == "account",
                        onClick = { bottomNavController.navigate("account") }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = bottomNavController,
                startDestination = "showcase",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("showcase") { ModernShowcaseScreen() }
                composable("cart") { CartScreen(navController = bottomNavController) }
                composable("account") { AccountScreen(email = email) }
            }
        }
    }
}

@Composable
fun ModernShowcaseScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                "⚙️ Bienvenido a AirSoft Rock Galactic",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Explora nuestras armas, equipamiento y accesorios de calidad profesional.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(24.dp))
        }

        // Ejemplo de productos destacados
        items(sampleProducts) { product ->
            ProductCardModern(product = product)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ProductCardModern(product: Product) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.precio)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = product.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(product.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(product.desc, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Text(formattedPrice, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    Toast.makeText(context, "${product.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Agregar")
                }
                OutlinedButton(onClick = { /* Comprar acción */ }) {
                    Text("Comprar")
                }
            }
        }
    }
}

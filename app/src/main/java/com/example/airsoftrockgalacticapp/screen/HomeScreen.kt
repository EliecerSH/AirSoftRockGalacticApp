package com.example.airsoftrockgalacticapp.screen

import android.annotation.SuppressLint
import android.provider.BaseColumns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.airsoftrockgalacticapp.data.ProductContract
import com.example.airsoftrockgalacticapp.data.ProductDbHelper
import com.example.airsoftrockgalacticapp.data.UserContract
import com.example.airsoftrockgalacticapp.data.UserDbHelper
import com.example.airsoftrockgalacticapp.data.CartDbHelper
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import com.example.airsoftrockgalacticapp.R

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
             ModalDrawerSheet(modifier = Modifier.fillMaxHeight(), drawerContainerColor = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Bienvenido, $userName", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }
                HorizontalDivider()
                NavigationDrawerItem(label = { Text("Armas") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }
                    bottomNavController.navigate("weapons")
                })
                NavigationDrawerItem(label = { Text("Vestimentas") }, selected = false, onClick = { scope.launch { drawerState.close() } })
                NavigationDrawerItem(label = { Text("Munición") }, selected = false, onClick = { scope.launch { drawerState.close() } })
                HorizontalDivider()
                NavigationDrawerItem(label = { Text("Contacto") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("contactanos")
                })
                NavigationDrawerItem(label = { Text("Nosotros") }, selected = false, onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("nosotros")
                })

                Spacer(Modifier.weight(1f))

                HorizontalDivider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión") },
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
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AirSoft Rock Galactic", color = MaterialTheme.colorScheme.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == "showcase",
                        onClick = { bottomNavController.navigate("showcase") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                        label = { Text("Carrito") },
                        selected = currentRoute == "cart",
                        onClick = { bottomNavController.navigate("cart") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                        label = { Text("Cuenta") },
                        selected = currentRoute == "account",
                        onClick = { bottomNavController.navigate("account") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    )
                }
            }
        ) { innerPadding ->
            NavHost(navController = bottomNavController, startDestination = "showcase", modifier = Modifier.padding(innerPadding)) {
                composable("showcase") { ShowcaseScreen(navController = bottomNavController) }
                composable("weapons") { WeaponsScreen(navController = bottomNavController) }
                composable("cart") { CartScreen(navController = bottomNavController) }
                composable("account") { AccountScreen(email = email) } // Pass email to AccountScreen
                composable(
                    "payment/{totalAmount}",
                    arguments = listOf(navArgument("totalAmount") { type = NavType.FloatType })
                ) { backStackEntry ->
                    PaymentScreen(
                        navController = bottomNavController,
                        totalAmount = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble(),
                        userEmail = email // Pass email to PaymentScreen
                    )
                }
            }
        }
    }
}

@Composable
fun ShowcaseScreen(navController: NavController) {
    val context = LocalContext.current
    val productDbHelper = ProductDbHelper(context)
    var popularProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = productDbHelper.readableDatabase
        val cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null, "2")
        val products = mutableListOf<Product>()
        with(cursor) {
            while (moveToNext()) {
                products.add(
                    Product(
                        id = getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                        slug = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_SLUG)),
                        nombre = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE)),
                        precio = getDouble(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_PRECIO)),
                        img = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_IMG)),
                        cantidad = getInt(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD)),
                        tipo = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_TIPO)),
                        desc = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_DESC))
                    )
                )
            }
            close()
        }
        popularProducts = products
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.img_01,
                    contentDescription = "Banner de la tienda",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Bienvenido a AirSoft Rock Galactic",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Calidad, rendimiento y la mejor selección para llevar tus partidas al siguiente nivel.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            Text(
                text = "Armas Populares",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (popularProducts.isNotEmpty()) {
                    Box(modifier = Modifier.weight(1f)) {
                        PopularProductCard(product = popularProducts[0], navController = navController)
                    }
                }
                if (popularProducts.size > 1) {
                    Box(modifier = Modifier.weight(1f)) {
                        PopularProductCard(product = popularProducts[1], navController = navController)
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PopularProductCard(product: Product, navController: NavController) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.precio)
    val cartDbHelper = CartDbHelper(context)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = if (imageResId != 0) imageResId else R.drawable.icon_01,
                contentDescription = product.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        cartDbHelper.addProductToCart(product.id)
                        navController.navigate("cart")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Comprar")
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.precio)
    val cartDbHelper = CartDbHelper(context)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                 if (imageResId != 0) {
                    AsyncImage(
                        model = if (imageResId != 0) imageResId else R.drawable.icon_01,
                        contentDescription = product.nombre,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(product.desc, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(formattedPrice, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    cartDbHelper.addProductToCart(product.id)
                    Toast.makeText(context, "${product.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Agregar al carrito")
                }
                Button(onClick = {
                    cartDbHelper.addProductToCart(product.id)
                    navController.navigate("cart")
                }) {
                    Text("Comprar")
                }
            }
        }
    }
}

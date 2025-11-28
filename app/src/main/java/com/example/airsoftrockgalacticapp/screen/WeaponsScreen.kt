package com.example.airsoftrockgalacticapp.screen

import android.provider.BaseColumns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.airsoftrockgalacticapp.R
import com.example.airsoftrockgalacticapp.data.CartDbHelper
import com.example.airsoftrockgalacticapp.data.ProductContract
import com.example.airsoftrockgalacticapp.data.ProductDbHelper
import com.example.airsoftrockgalacticapp.model.Product
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponsScreen(navController: NavController) {
    val context = LocalContext.current
    val dbHelper = ProductDbHelper(context)
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // Cargar productos desde la base de datos
    LaunchedEffect(Unit) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null, null, null, null, null, null
        )
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
        productList = products
    }

    // Filtrado de productos
    val filteredList = if (searchQuery.isEmpty()) productList
    else productList.filter { it.nombre.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Armas disponibles",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(filteredList) { index, product ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                ) {
                    ProductCard(product, navController)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(product.img, "drawable", context.packageName)
    val priceFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val cartDbHelper = CartDbHelper(context)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del producto
            Image(
                painter = painterResource(
                    id = if (imageResId != 0) imageResId else R.drawable.icon_01
                ),
                contentDescription = product.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = priceFormat.format(product.precio),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        cartDbHelper.addProductToCart(product.id)
                        Toast.makeText(context, "${product.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Agregar al carrito", color = MaterialTheme.colorScheme.onSecondary)
                }

                Button(
                    onClick = {
                        cartDbHelper.addProductToCart(product.id)
                        navController.navigate("cart")
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Comprar", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
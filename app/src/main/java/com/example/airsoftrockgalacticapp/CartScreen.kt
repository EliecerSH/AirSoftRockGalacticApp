package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import android.provider.BaseColumns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.Locale

data class CartItem(val product: Product, val quantity: Int)

@SuppressLint("Range")
@Composable
fun CartScreen(navController: NavController) {
    val context = LocalContext.current
    val cartDbHelper = CartDbHelper(context)
    val productDbHelper = ProductDbHelper(context)
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var subtotal by remember { mutableDoubleStateOf(0.0) }
    val iva = subtotal * 0.19
    val total = subtotal + iva

    // Function to load cart items
    @SuppressLint("Range")
    fun loadCartItems() {
        val items = mutableListOf<CartItem>()
        val cursor = cartDbHelper.getCartItems()
        var newSubtotal = 0.0
        with(cursor) {
            while (moveToNext()) {
                val productId = getLong(getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID))
                val quantity = getInt(getColumnIndex(CartContract.CartEntry.COLUMN_NAME_QUANTITY))

                // Get product details from ProductDbHelper
                val productCursor = productDbHelper.readableDatabase.query(
                    ProductContract.ProductEntry.TABLE_NAME,
                    null,
                    "${BaseColumns._ID} = ?",
                    arrayOf(productId.toString()),
                    null, null, null
                )

                with(productCursor) {
                    if (moveToFirst()) {
                        val product = Product(
                            id = getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                            slug = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_SLUG)),
                            nombre = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_NOMBRE)),
                            precio = getDouble(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_PRECIO)),
                            img = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_IMG)),
                            cantidad = getInt(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_CANTIDAD)),
                            tipo = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_TIPO)),
                            desc = getString(getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME_DESC))
                        )
                        items.add(CartItem(product, quantity))
                        newSubtotal += product.precio * quantity
                    }
                    close()
                }
            }
            close()
        }
        cartItems = items
        subtotal = newSubtotal
    }

    // Load cart items initially and whenever the screen is shown
    LaunchedEffect(Unit) {
        loadCartItems()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { cartItem ->
                    CartItemCard(cartItem)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            Text("Subtotal: ${currencyFormat.format(subtotal)}", style = MaterialTheme.typography.bodyLarge)
            Text("IVA (19%): ${currencyFormat.format(iva)}", style = MaterialTheme.typography.bodyLarge)
            Text("Total: ${currencyFormat.format(total)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { 
                    cartDbHelper.clearCart()
                    loadCartItems() // Refresh the cart screen
                }) {
                    Text("Vaciar Carrito")
                }
                Button(onClick = { navController.navigate("payment/$total") }) {
                    Text("Proceder al Pago")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(cartItem: CartItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.product.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Cantidad: ${cartItem.quantity}")
            }
            Text(NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(cartItem.product.precio * cartItem.quantity), fontWeight = FontWeight.Bold)
        }
    }
}
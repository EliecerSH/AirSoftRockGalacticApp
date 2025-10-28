package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import android.provider.BaseColumns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    // --- Función para cargar los productos del carrito ---
    @SuppressLint("Range")
    fun loadCartItems() {
        val items = mutableListOf<CartItem>()
        val cursor = cartDbHelper.getCartItems()
        var newSubtotal = 0.0
        with(cursor) {
            while (moveToNext()) {
                val productId = getLong(getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PRODUCT_ID))
                val quantity = getInt(getColumnIndex(CartContract.CartEntry.COLUMN_NAME_QUANTITY))

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

    // Cargar al iniciar
    LaunchedEffect(Unit) {
        loadCartItems()
    }

    // --- Diseño principal ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Tu Carrito",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = cartItems.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        AnimatedVisibility(
            visible = cartItems.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { cartItem ->
                    CartItemCard(cartItem)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if (cartItems.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Subtotal: ${currencyFormat.format(subtotal)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "IVA (19%): ${currencyFormat.format(iva)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Total: ${currencyFormat.format(total)}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        cartDbHelper.clearCart()
                        loadCartItems()
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Vaciar", color = MaterialTheme.colorScheme.onBackground)
                }

                Button(
                    onClick = { navController.navigate("payment/$total") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pagar")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(cartItem: CartItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.product.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Cantidad: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                )
            }
            Text(
                text = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                    .format(cartItem.product.precio * cartItem.quantity),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

package com.example.airsoftrockgalacticapp

import android.provider.BaseColumns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WeaponsScreen() {
    val context = LocalContext.current
    val dbHelper = ProductDbHelper(context)
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(ProductContract.ProductEntry.TABLE_NAME, null, null, null, null, null, null)
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

    val filteredList = if (searchQuery.isEmpty()) {
        productList
    } else {
        productList.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(filteredList) { product ->
                ProductCard(product)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
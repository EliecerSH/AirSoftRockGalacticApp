package com.example.airsoftrockgalacticapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.BaseColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class Payment(val id: Long, val totalAmount: Double, val date: String, val name: String, val address: String, val cardNumber: String)

@SuppressLint("Range")
@Composable
fun AccountScreen(email: String?) {
    val context = LocalContext.current
    val userDbHelper = UserDbHelper(context)
    val paymentDbHelper = PaymentDbHelper(context)
    val themeDataStore = remember { ThemeDataStore(context) }
    val isDarkMode by themeDataStore.isDarkMode.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var payments by remember { mutableStateOf<List<Payment>>(emptyList()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(it, flag)
                avatarUri = it
                 if (email != null) {
                    val db = userDbHelper.writableDatabase
                    val values = ContentValues().apply {
                        put(UserContract.UserEntry.COLUMN_NAME_AVATAR_URI, it.toString())
                    }
                    db.update(UserContract.UserEntry.TABLE_NAME, values, "${UserContract.UserEntry.COLUMN_NAME_EMAIL} = ?", arrayOf(email))
                }
            }
        }
    )

    LaunchedEffect(email) {
        if (email != null) {
            val userDb = userDbHelper.readableDatabase
            val userCursor = userDb.query(UserContract.UserEntry.TABLE_NAME, arrayOf(UserContract.UserEntry.COLUMN_NAME_NAME, UserContract.UserEntry.COLUMN_NAME_ALIAS, UserContract.UserEntry.COLUMN_NAME_AVATAR_URI), "${UserContract.UserEntry.COLUMN_NAME_EMAIL} = ?", arrayOf(email), null, null, null)
            with(userCursor) {
                if (moveToFirst()) {
                    name = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_NAME))
                    alias = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_ALIAS)) ?: ""
                    val uriString = getString(getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_AVATAR_URI))
                    if (!uriString.isNullOrEmpty()) {
                        avatarUri = Uri.parse(uriString)
                    }
                }
                close()
            }

            val paymentDb = paymentDbHelper.readableDatabase
            val paymentCursor = paymentDb.query(PaymentContract.PaymentEntry.TABLE_NAME, null, "${PaymentContract.PaymentEntry.COLUMN_NAME_USER_EMAIL} = ?", arrayOf(email), null, null, "${BaseColumns._ID} DESC")
            val paymentList = mutableListOf<Payment>()
            with(paymentCursor) {
                while (moveToNext()) {
                    paymentList.add(
                        Payment(
                            id = getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                            totalAmount = getDouble(getColumnIndexOrThrow(PaymentContract.PaymentEntry.COLUMN_NAME_TOTAL_AMOUNT)),
                            date = getString(getColumnIndexOrThrow(PaymentContract.PaymentEntry.COLUMN_NAME_PURCHASE_DATE)),
                            name = getString(getColumnIndexOrThrow(PaymentContract.PaymentEntry.COLUMN_NAME_NAME)),
                            address = getString(getColumnIndexOrThrow(PaymentContract.PaymentEntry.COLUMN_NAME_ADDRESS)),
                            cardNumber = getString(getColumnIndexOrThrow(PaymentContract.PaymentEntry.COLUMN_NAME_CARD_NUMBER))
                        )
                    )
                }
                close()
            }
            payments = paymentList
        }
    }

    fun updateUserAlias() {
        if (email != null) {
            val db = userDbHelper.writableDatabase
            val values = ContentValues().apply {
                put(UserContract.UserEntry.COLUMN_NAME_ALIAS, alias)
            }
            db.update(UserContract.UserEntry.TABLE_NAME, values, "${UserContract.UserEntry.COLUMN_NAME_EMAIL} = ?", arrayOf(email))
            Toast.makeText(context, "Alias actualizado", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // --- User Profile Section ---
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (avatarUri != null) {
                        AsyncImage(model = avatarUri, contentDescription = "Avatar", modifier = Modifier.size(150.dp).clip(CircleShape).clickable { imagePickerLauncher.launch("image/*") }, contentScale = ContentScale.Crop)
                    } else {
                        Image(painter = painterResource(id = R.drawable.icon_01), contentDescription = "Avatar", modifier = Modifier.size(150.dp).clip(CircleShape).clickable { imagePickerLauncher.launch("image/*") }, contentScale = ContentScale.Crop)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(email ?: "", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = alias, onValueChange = { alias = it }, label = { Text("Alias") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { updateUserAlias() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Guardar Alias")
                    }
                }
            }
        }

        // --- Theme Switcher ---
        item {
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Modo Oscuro", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { scope.launch { themeDataStore.setDarkMode(it) } }
                    )
                }
            }
        }

        // --- Purchase History Section ---
        item {
            Text("Mis Compras", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 8.dp))
        }
        if (payments.isEmpty()) {
            item {
                Text("No tienes compras registradas.")
            }
        } else {
            items(payments) { payment ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Compra #${payment.id}", style = MaterialTheme.typography.titleMedium)
                        Text("Fecha: ${payment.date}")
                        Text("Nombre: ${payment.name}")
                        Text("Dirección: ${payment.address}")
                        Text("Tarjeta: **** **** **** ${payment.cardNumber.takeLast(4)}")
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text(NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(payment.totalAmount), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
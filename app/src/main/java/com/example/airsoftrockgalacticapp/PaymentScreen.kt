package com.example.airsoftrockgalacticapp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentScreen(navController: NavController, totalAmount: Double?, userEmail: String?) {
    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var commune by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val context = LocalContext.current
    val paymentDbHelper = PaymentDbHelper(context)
    val cartDbHelper = CartDbHelper(context)
    val formattedTotal = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(totalAmount ?: 0.0)

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            // --- Datos Personales ---
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Datos Personales", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                }
            }

            // --- Dirección ---
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dirección de Envío", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Región") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = commune, onValueChange = { commune = it }, label = { Text("Comuna") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                }
            }

            // --- Método de Pago ---
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Método de Pago", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = cardNumber, onValueChange = { cardNumber = it }, label = { Text("Número de Tarjeta") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = cardHolderName, onValueChange = { cardHolderName = it }, label = { Text("Nombre del Titular") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = expiryDate, onValueChange = { expiryDate = it }, label = { Text("Fecha de Vencimiento (MM/AA)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = cvv, onValueChange = { cvv = it }, label = { Text("CVV") }, modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Monto a Pagar: $formattedTotal", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && rut.isNotBlank() && phone.isNotBlank() && region.isNotBlank() && commune.isNotBlank() && address.isNotBlank() && cardNumber.isNotBlank() && cardHolderName.isNotBlank() && expiryDate.isNotBlank() && cvv.isNotBlank() && totalAmount != null && userEmail != null) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val currentDate = sdf.format(Date())
                        paymentDbHelper.addPayment(name, rut, phone, userEmail, region, commune, address, cardNumber, cardHolderName, expiryDate, cvv, totalAmount, currentDate)
                        cartDbHelper.clearCart()
                        Toast.makeText(context, "Pago realizado con éxito", Toast.LENGTH_LONG).show()
                        navController.navigate("showcase") {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pagar")
            }
        }
    }
}
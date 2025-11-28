package com.example.airsoftrockgalacticapp.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airsoftrockgalacticapp.data.CartDbHelper
import com.example.airsoftrockgalacticapp.data.PaymentDbHelper
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Finalizar Compra",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // --- Datos Personales ---
            item {
                AnimatedVisibility(visible = true, enter = fadeIn()) {
                    PaymentCard(title = "Datos Personales") {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            // --- Dirección ---
            item {
                AnimatedVisibility(visible = true, enter = fadeIn()) {
                    PaymentCard(title = "Dirección de Envío") {
                        OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Región") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = commune, onValueChange = { commune = it }, label = { Text("Comuna") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            // --- Método de Pago ---
            item {
                AnimatedVisibility(visible = true, enter = fadeIn()) {
                    PaymentCard(title = "Método de Pago") {
                        OutlinedTextField(value = cardNumber, onValueChange = { cardNumber = it }, label = { Text("Número de Tarjeta") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = cardHolderName, onValueChange = { cardHolderName = it }, label = { Text("Nombre del Titular") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = expiryDate, onValueChange = { expiryDate = it }, label = { Text("Fecha de Vencimiento (MM/AA)") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = cvv, onValueChange = { cvv = it }, label = { Text("CVV") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            // --- Total y Botón de Pago ---
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Monto a pagar: $formattedTotal",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Button(
                        onClick = {
                            if (name.isNotBlank() && rut.isNotBlank() && phone.isNotBlank() &&
                                region.isNotBlank() && commune.isNotBlank() && address.isNotBlank() &&
                                cardNumber.isNotBlank() && cardHolderName.isNotBlank() &&
                                expiryDate.isNotBlank() && cvv.isNotBlank() &&
                                totalAmount != null && userEmail != null
                            ) {
                                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                val currentDate = sdf.format(Date())
                                paymentDbHelper.addPayment(
                                    name, rut, phone, userEmail,
                                    region, commune, address,
                                    cardNumber, cardHolderName, expiryDate, cvv,
                                    totalAmount, currentDate
                                )
                                cartDbHelper.clearCart()
                                Toast.makeText(context, "Pago realizado con éxito", Toast.LENGTH_LONG).show()
                                navController.navigate("showcase") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Pagar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )
            content()
        }
    }
}
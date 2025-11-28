package com.example.airsoftrockgalacticapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airsoftrockgalacticapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NosotrosScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nosotros", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.icon_01),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
            )

            Spacer(Modifier.height(12.dp))

            // TÍTULO PRINCIPAL
            Text(
                text = "AirSoft Rock Galactic",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Pasión por el Airsoft",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // CARD MISIÓN
            InfoCard(
                title = "Nuestra Misión",
                description = "En AirSoft Rock Galactic buscamos elevar la experiencia del airsoft al máximo nivel. Ofrecemos equipamiento de alta calidad, réplicas de precisión y accesorios tácticos para que disfrutes cada partida al máximo."
            )

            Spacer(Modifier.height(16.dp))

            // CARD APP
            InfoCard(
                title = "¿Qué ofrece nuestra App?",
                description = "Explora nuestro catálogo actualizado, realiza compras seguras, revisa tus pedidos y recibe notificaciones de ofertas exclusivas. Todo en una plataforma rápida, intuitiva y diseñada para jugadores como tú."
            )

            Spacer(Modifier.height(32.dp))

            Divider(modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))

            Text(
                text = "¡Equípate, prepárate y vive la experiencia AirSoft Rock Galactic!",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun InfoCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )
        }
    }
}

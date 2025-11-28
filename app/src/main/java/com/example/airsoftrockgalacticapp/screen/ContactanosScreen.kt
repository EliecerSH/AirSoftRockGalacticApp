package com.example.airsoftrockgalacticapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airsoftrockgalacticapp.MainViewModel
import com.example.airsoftrockgalacticapp.model.Comentarios

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactanosScreen(viewModel: MainViewModel, navController: NavController) {

    val comentarios by viewModel.comentarios.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var currentComentario by remember { mutableStateOf<Comentarios?>(null) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var comentarioToDelete by remember { mutableStateOf<Comentarios?>(null) }

    val filteredComentarios = if (searchQuery.isEmpty()) comentarios else comentarios.filter {
        it.tematica.contains(searchQuery, true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                currentComentario = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, "Añadir")
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar por temática") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredComentarios) { comentario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Nombre: ${comentario.nombre}")
                            Text("Contenido: ${comentario.contenido}")
                            Text("Temática: ${comentario.tematica}")

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {

                                IconButton(onClick = {
                                    currentComentario = comentario
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, "Editar")
                                }

                                IconButton(onClick = {
                                    comentarioToDelete = comentario
                                    showDeleteConfirmDialog = true
                                }) {
                                    Icon(Icons.Default.Delete, "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIÁLOGO CREAR/EDITAR
    if (showDialog) {
        ComentarioDialog(
            comentario = currentComentario,
            onDismiss = { showDialog = false },
            onSave = { newComentario ->
                if (currentComentario == null)
                    viewModel.crearComentario(newComentario)
                else
                    viewModel.actualizarComentario(
                        currentComentario!!.id_comentario!!,
                        newComentario
                    )

                showDialog = false
            }
        )
    }

    // CONFIRMACIÓN ELIMINACIÓN
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que deseas eliminar el comentario?") },
            confirmButton = {
                Button(onClick = {
                    comentarioToDelete?.let {
                        viewModel.eliminarComentario(it.id_comentario!!)
                    }
                    showDeleteConfirmDialog = false
                }) { Text("Eliminar") }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ComentarioDialog(
    comentario: Comentarios?,
    onDismiss: () -> Unit,
    onSave: (Comentarios) -> Unit
) {
    var nombre by remember { mutableStateOf(comentario?.nombre ?: "") }
    var contenido by remember { mutableStateOf(comentario?.contenido ?: "") }
    var tematica by remember { mutableStateOf(comentario?.tematica ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (comentario == null) "Nuevo Comentario" else "Editar Comentario")
        },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(contenido, { contenido = it }, label = { Text("Contenido") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(tematica, { tematica = it }, label = { Text("Temática") })
            }
        },
        confirmButton = {
            Button(onClick = {

                val newComentario =
                    if (comentario == null) {
                        // ✔️ NO enviar ID → backend genera uno
                        Comentarios(
                            id_comentario = null,
                            nombre = nombre,
                            contenido = contenido,
                            tematica = tematica
                        )
                    } else {
                        Comentarios(
                            id_comentario = comentario.id_comentario,
                            nombre = nombre,
                            contenido = contenido,
                            tematica = tematica
                        )
                    }

                onSave(newComentario)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}



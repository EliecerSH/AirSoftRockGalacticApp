package com.example.airsoftrockgalacticapp.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.airsoftrockgalacticapp.MainViewModel
import com.example.airsoftrockgalacticapp.model.Comentarios

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactanosScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val comentarios by viewModel.comentarios.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var currentComentario by remember { mutableStateOf<Comentarios?>(null) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var comentarioToDelete by remember { mutableStateOf<Comentarios?>(null) }

    // Animated visibility for the whole content when the screen appears
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    val filteredComentarios = if (searchQuery.isEmpty()) comentarios else comentarios.filter {
        it.tematica.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Contacto", style = MaterialTheme.typography.titleLarge)
                        Text("${comentarios.size} comentarios", style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Añadir") },
                icon = { Icon(Icons.Default.Add, contentDescription = "Añadir") },
                onClick = {
                    currentComentario = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(350)) + slideInVertically(animationSpec = tween(350), initialOffsetY = { it / 6 }),
            exit = fadeOut(animationSpec = tween(250)) + slideOutVertically(animationSpec = tween(250), targetOffsetY = { it / 6 })
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar por temática") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Empty state
                if (filteredComentarios.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 40.dp)) {
                            Text("No hay comentarios", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Agrega el primero usando el botón \"Añadir\"", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                        }
                    }
                } else {
                    // List of comments
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredComentarios, key = { it.id_comentario ?: it.hashCode() }) { comentario ->
                            // Each card has its own entry animation
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = tween(300), initialOffsetY = { it / 8 })
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // quick edit on tap
                                            currentComentario = comentario
                                            showDialog = true
                                        },
                                    shape = RoundedCornerShape(14.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(text = comentario.nombre, style = MaterialTheme.typography.titleMedium)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = comentario.tematica,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            // Action icons
                                            Row {
                                                IconButton(onClick = {
                                                    currentComentario = comentario
                                                    showDialog = true
                                                }) {
                                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                                }
                                                IconButton(onClick = {
                                                    comentarioToDelete = comentario
                                                    showDeleteConfirmDialog = true
                                                }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = comentario.contenido,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Create / Edit dialog
    if (showDialog) {
        ComentarioDialog(
            comentario = currentComentario,
            onDismiss = { showDialog = false },
            onSave = { newComentario ->
                if (currentComentario == null) {
                    // create: id_comentario must be null so backend generates it
                    viewModel.crearComentario(newComentario)
                } else {
                    // update: pass existing id
                    viewModel.actualizarComentario(currentComentario!!.id_comentario!!, newComentario)
                }
                showDialog = false
            }

        )
    }

    // Delete confirmation
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que deseas eliminar el comentario?") },
            confirmButton = {
                Button(
                    onClick = {
                        comentarioToDelete?.let {
                            viewModel.eliminarComentario(it.id_comentario!!)
                        }
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
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
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = contenido,
                    onValueChange = { contenido = it },
                    label = { Text("Contenido") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = tematica,
                    onValueChange = { tematica = it },
                    label = { Text("Temática") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    val newComentario = if (comentario == null) {
                        // IMPORTANT: do not send id = 0 -> let backend create it
                        Comentarios(id_comentario = null, nombre = nombre, contenido = contenido, tematica = tematica)
                    } else {
                        Comentarios(id_comentario = comentario.id_comentario, nombre = nombre, contenido = contenido, tematica = tematica)
                    }
                    onSave(newComentario)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


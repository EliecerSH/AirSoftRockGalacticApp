package com.example.airsoftrockgalacticapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airsoftrockgalacticapp.model.Comentarios
import com.example.airsoftrockgalacticapp.network.ApiComentarioService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    private val _comentarios = MutableStateFlow<List<Comentarios>>(emptyList())
    val comentarios: StateFlow<List<Comentarios>> = _comentarios

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://cliente-service-arg.onrender.com/") // corregido
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiComentarioService::class.java)

    init {
        cargarComentarios()
    }

    fun cargarComentarios() {
        viewModelScope.launch {
            try {
                _comentarios.value = api.getComentarios()
            } catch (e: Exception) {
                _comentarios.value = listOf(
                    Comentarios(0, "Error", "No se pudieron cargar los comentarios", "Error")
                )
            }
        }
    }

    fun crearComentario(comentario: Comentarios) {
        viewModelScope.launch {
            try {
                api.createComentario(comentario)
                cargarComentarios()
            } catch (_: Exception) {}
        }
    }

    fun actualizarComentario(id: Int, comentario: Comentarios) {
        viewModelScope.launch {
            try {
                api.updateComentario(id, comentario)
                cargarComentarios()
            } catch (_: Exception) {}
        }
    }

    fun eliminarComentario(id: Int) {
        viewModelScope.launch {
            try {
                api.deleteComentario(id)
                cargarComentarios()
            } catch (_: Exception) {}
        }
    }
}


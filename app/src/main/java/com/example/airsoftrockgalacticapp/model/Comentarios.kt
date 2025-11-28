package com.example.airsoftrockgalacticapp.model

data class Comentarios(
    val id_comentario: Int? = null,   // ID opcional
    val nombre: String,
    val contenido: String,
    val tematica: String
)

package com.example.airsoftrockgalacticapp.model

data class Product(
    val id: Long,
    val slug: String,
    val nombre: String,
    val precio: Double,
    val img: String,
    val cantidad: Int,
    val tipo: String,
    val desc: String
)
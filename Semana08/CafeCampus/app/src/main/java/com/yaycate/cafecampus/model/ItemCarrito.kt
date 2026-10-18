package com.yaycate.cafecampus.model

data class ItemCarrito(
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val nota: String = ""
)
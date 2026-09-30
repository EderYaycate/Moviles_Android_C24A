package com.yaycate.tecsupstore

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String
)

val categorias = listOf("Todas", "Tecnología", "Ropa", "Accesorios")

val productosEjemplo = listOf(
    Producto(1, "Audífonos Bluetooth", 89.90, "Tecnología"),
    Producto(2, "Mouse inalámbrico", 45.00, "Tecnología"),
    Producto(3, "Polo Tecsup", 35.00, "Ropa"),
    Producto(4, "Casaca deportiva", 120.00, "Ropa"),
    Producto(5, "Mochila", 75.50, "Accesorios"),
    Producto(6, "Gorra", 25.00, "Accesorios")
)
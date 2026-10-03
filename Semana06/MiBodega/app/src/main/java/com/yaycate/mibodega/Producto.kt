package com.yaycate.mibodega

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String
)

val productosEjemplo = listOf(
    Producto(1, "Inca Kola 1.5L", 7.50, "Bebidas"),
    Producto(2, "Agua San Luis 625ml", 1.50, "Bebidas"),
    Producto(3, "Arroz Costeño 1kg", 5.20, "Abarrotes"),
    Producto(4, "Aceite Primor 1L", 9.80, "Abarrotes"),
    Producto(5, "Fideos Don Vittorio 500g", 3.60, "Abarrotes"),
    Producto(6, "Papas Lays Clásicas", 2.50, "Snacks"),
    Producto(7, "Galletas Oreo", 1.80, "Snacks"),
    Producto(8, "Chizitos", 1.20, "Snacks")
)
package com.yaycate.mibodega.ui.cliente.modelo

import com.yaycate.mibodega.R

val listaCategorias = listOf("Todos", "Abarrotes", "Bebidas", "Lácteos", "Snacks")

val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz Costeño",
        descripcion = "Arroz extra 1kg",
        precio = 4.50,
        categoria = "Abarrotes",
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal 1L",
        precio = 8.90,
        categoria = "Abarrotes",
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada 390g",
        precio = 5.20,
        categoria = "Lácteos",
        imagenRes = R.drawable.ilustracion_bodega
    ),
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Paquete familiar",
        precio = 3.50,
        categoria = "Snacks",
        imagenRes = R.drawable.ilustracion_bodega
    )
)
package com.yaycate.mibodega.ui.cliente.modelo

import com.yaycate.mibodega.R

object DatosFake {
    val productos = listOf(
        Producto(
            id = 1,
            nombre = "Arroz Costeño",
            presentacion = "1 kg",
            descripcion = "Arroz extra de grano largo, ideal para el día a día.",
            precio = 4.50,
            categoria = "Abarrotes",
            imagenRes = R.drawable.img_1
        ),
        Producto(
            id = 2,
            nombre = "Aceite Primor",
            presentacion = "1 L",
            descripcion = "Aceite vegetal para cocinar y freír.",
            precio = 8.90,
            categoria = "Abarrotes",
            imagenRes = R.drawable.img_2
        ),
        Producto(
            id = 3,
            nombre = "Leche Gloria",
            presentacion = "390 g",
            descripcion = "Leche evaporada entera.",
            precio = 5.20,
            categoria = "Bebidas",
            imagenRes = R.drawable.img_3

        ),
        Producto(
            id = 4,
            nombre = "Galleta Oreo",
            presentacion = "126 g",
            descripcion = "Galletas de chocolate con crema.",
            precio = 3.50,
            categoria = "Snacks",
            imagenRes = R.drawable.img
        )
    )
}
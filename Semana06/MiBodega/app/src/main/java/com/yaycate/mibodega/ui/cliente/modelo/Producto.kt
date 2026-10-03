package com.yaycate.mibodega.ui.cliente.modelo

import androidx.annotation.DrawableRes

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    @DrawableRes val imagenRes: Int? = null
)
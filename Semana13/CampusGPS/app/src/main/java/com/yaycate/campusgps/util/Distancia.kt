package com.yaycate.campusgps.util

import com.yaycate.campusgps.model.Posicion
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun distanciaMetros(origen: Posicion, destino: Posicion): Float {
    val radioTierra = 6371000.0
    val dLat = Math.toRadians(destino.latitud - origen.latitud)
    val dLon = Math.toRadians(destino.longitud - origen.longitud)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(origen.latitud)) * cos(Math.toRadians(destino.latitud)) *
            sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return (radioTierra * c).toFloat()
}
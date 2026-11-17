package com.yaycate.campusgps.util

fun formatearDistancia(metros: Float?): String = when {
    metros == null -> "—"
    metros < 1000f -> "${metros.toInt()} m"
    else -> "%.1f km".format(metros / 1000f)
}
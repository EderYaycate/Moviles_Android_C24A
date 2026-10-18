package com.yaycate.cafecampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ProductoIcono(icono: String, modifier: Modifier = Modifier) {
    Icon(
        imageVector = iconoDe(icono),
        contentDescription = null,
        modifier = modifier
    )
}

private fun iconoDe(clave: String): ImageVector = when (clave.lowercase()) {
    "cafe" -> Icons.Default.LocalCafe
    "plato" -> Icons.Default.Restaurant
    "jugo", "botella" -> Icons.Default.LocalDrink
    "pan" -> Icons.Default.BakeryDining
    else -> Icons.Default.Restaurant
}
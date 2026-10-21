package com.yaycate.cafecampus.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import java.text.Normalizer

fun recursoImagen(context: Context, nombre: String): Int {
    if (nombre.isBlank()) return 0
    return try {
        context.resources.getIdentifier(nombre, "drawable", context.packageName)
    } catch (e: Exception) {
        0
    }
}

fun slugProducto(nombre: String): String {
    val sinAcentos = Normalizer.normalize(nombre, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    return sinAcentos.lowercase()
        .replace("[^a-z0-9\\s_]".toRegex(), "")
        .trim()
        .replace("\\s+".toRegex(), "_")
}

fun obtenerIniciales(nombre: String): String {
    val partes = nombre.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    return when {
        partes.isEmpty() -> "?"
        partes.size == 1 -> partes[0].take(2).uppercase()
        else -> (partes[0].take(1) + partes[1].take(1)).uppercase()
    }
}

@Composable
fun ImagenCircular(
    recurso: Int,
    fallback: ImageVector,
    tamaño: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    if (recurso != 0) {
        Image(
            painter = painterResource(id = recurso),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(tamaño)
                .clip(CircleShape)
        )
    } else {
        Box(
            modifier = modifier
                .size(tamaño)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = fallback,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(tamaño * 0.55f)
            )
        }
    }
}
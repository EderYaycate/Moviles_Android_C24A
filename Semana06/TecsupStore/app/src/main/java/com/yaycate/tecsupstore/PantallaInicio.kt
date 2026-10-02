package com.yaycate.tecsupstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaInicio(
    favoritos: List<Int>,
    onToggleFavorito: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var categoriaActual by remember { mutableStateOf("Todas") }
    val filtrados = if (categoriaActual == "Todas") {
        productosEjemplo
    } else {
        productosEjemplo.filter { it.categoria == categoriaActual }
    }

    Column(modifier = modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { cat ->
                FilterChip(
                    selected = cat == categoriaActual,
                    onClick = { categoriaActual = cat },
                    label = { Text(cat) }
                )
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtrados, key = { it.id }) { producto ->
                TarjetaProducto(
                    producto = producto,
                    esFavorito = producto.id in favoritos,
                    onFavoritoClick = { onToggleFavorito(producto.id) }
                )
            }
        }
    }
}
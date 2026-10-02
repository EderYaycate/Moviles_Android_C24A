package com.yaycate.tecsupstore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaProducto(

    producto: Producto,
    esFavorito: Boolean,
    onFavoritoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("S/ ${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                Text(producto.categoria, style = MaterialTheme.typography.labelSmall)
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(if (esFavorito) "Quitar de favoritos" else "Favoritos")
                        },

                        leadingIcon = {
                            Icon(
                                imageVector = if (esFavorito) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            onFavoritoClick()
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        leadingIcon = {
                            Icon(Icons.Default.Share, contentDescription = null)
                        },
                        onClick = { expanded = false }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        leadingIcon = {
                            Icon(Icons.Default.Warning, contentDescription = null)
                        },
                        onClick = { expanded = false }
                    )
                }
            }
        }
    }

}
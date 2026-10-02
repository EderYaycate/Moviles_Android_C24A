package com.yaycate.tecsupstore

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class Destino(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

val destinos = listOf(
    Destino("inicio", "Inicio", Icons.Default.Home),
    Destino("pedidos", "Mis pedidos", Icons.Default.ShoppingCart),
    Destino("favoritos", "Favoritos", Icons.Default.Favorite),
    Destino("perfil", "Perfil", Icons.Default.Person)
)

@Composable
fun AppDrawer(onItemClick: (Destino) -> Unit) {
    ModalDrawerSheet {
        destinos.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                icon = { Icon(destino.icono, contentDescription = null) },
                selected = false,
                onClick = { onItemClick(destino) }
            )
        }
    }
}
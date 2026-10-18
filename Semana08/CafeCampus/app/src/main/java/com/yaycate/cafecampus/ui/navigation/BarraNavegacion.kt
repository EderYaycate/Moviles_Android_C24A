package com.yaycate.cafecampus.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class ItemNavegacion(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

val itemsCliente = listOf(
    ItemNavegacion(Ruta.Menu.ruta, "Menú", Icons.Default.RestaurantMenu),
    ItemNavegacion(Ruta.Pedidos.ruta, "Pedidos", Icons.Default.List),
    ItemNavegacion(Ruta.Ajustes.ruta, "Perfil", Icons.Default.Person)
)

val itemsAdmin = listOf(
    ItemNavegacion(Ruta.Panel.ruta, "Panel", Icons.Default.Home),
    ItemNavegacion(Ruta.PedidosAdmin.ruta, "Pedidos", Icons.Default.List),
    ItemNavegacion(Ruta.MenuAdmin.ruta, "Menú", Icons.Default.RestaurantMenu),
    ItemNavegacion(Ruta.Ajustes.ruta, "Ajustes", Icons.Default.Settings)
)

@Composable
fun BarraNavegacion(
    items: List<ItemNavegacion>,
    rutaActual: String?,
    onSeleccion: (String) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { onSeleccion(item.ruta) },
                icon = { Icon(imageVector = item.icono, contentDescription = item.etiqueta) },
                label = { Text(item.etiqueta) }
            )
        }
    }
}
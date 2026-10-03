package com.yaycate.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val MoradoPrincipal = Color(0xFF5E2D91)
private val LilaAvatar = Color(0xFFE6D9F5)
private val LilaSeleccion = Color(0xFFEBDDF7)
private val TextoOscuro = Color(0xFF1C1B1F)
private val TextoGris = Color(0xFF6B6B6B)

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
fun EncabezadoDrawer(nombre: String, correo: String) {
    val iniciales = nombre
        .split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull() }
        .joinToString("")
        .uppercase()

    Column(modifier = Modifier.padding(24.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(LilaAvatar),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iniciales,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MoradoPrincipal
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            nombre,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )
        Text(
            correo,
            style = MaterialTheme.typography.bodySmall,
            color = TextoGris
        )
    }
}

@Composable
fun AppDrawer(
    rutaActual: String?,
    cantidadFavoritos: Int,
    onItemClick: (Destino) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val colores = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = LilaSeleccion,
        selectedTextColor = MoradoPrincipal,
        selectedIconColor = MoradoPrincipal,
        unselectedTextColor = TextoOscuro,
        unselectedIconColor = TextoGris
    )

    ModalDrawerSheet(drawerContainerColor = Color.White) {
        EncabezadoDrawer(
            nombre = "Estudiante TECSUP",
            correo = "usuario@tecsup.edu.pe"
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        destinos.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                icon = { Icon(destino.icono, contentDescription = null) },
                badge = {
                    if (destino.ruta == "favoritos" && cantidadFavoritos > 0) {
                        Badge { Text(cantidadFavoritos.toString()) }
                    }
                },
                selected = destino.ruta == rutaActual,
                onClick = { onItemClick(destino) },
                colors = colores,
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            icon = {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            },
            selected = false,
            onClick = onCerrarSesion,
            colors = colores,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
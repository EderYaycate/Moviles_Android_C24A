package com.yaycate.tecsupfit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomItem(
    val ruta: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun TecsupBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomItem("inicio", "Inicio", Icons.Filled.Home),
        BottomItem("reservas", "Reservas", Icons.Filled.EventAvailable),
        BottomItem("rutinas", "Rutinas", Icons.Filled.FitnessCenter),
        BottomItem("perfil", "Perfil", Icons.Filled.Person)
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.ruta,
                onClick = {
                    if (currentRoute != item.ruta) {
                        navController.navigate(item.ruta) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
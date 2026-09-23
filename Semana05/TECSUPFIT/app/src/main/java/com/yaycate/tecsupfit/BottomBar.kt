package com.yaycate.tecsupfit.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inicio : BottomNavItem("inicio", "Inicio", Icons.Default.Home)
    object Reservas : BottomNavItem("reservas", "Reservas", Icons.Default.DateRange)
    object Rutinas : BottomNavItem("rutinas", "Rutinas", Icons.Default.FitnessCenter)
    object Perfil : BottomNavItem("perfil", "Perfil", Icons.Default.Person)
}

@Composable
fun TecsupBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Reservas,
        BottomNavItem.Rutinas,
        BottomNavItem.Perfil
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF005A44),
                    selectedTextColor = Color(0xFF005A44),
                    indicatorColor = Color(0xFFE8F5E9),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
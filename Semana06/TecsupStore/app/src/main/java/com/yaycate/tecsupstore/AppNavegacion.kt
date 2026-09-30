package com.yaycate.tecsupstore

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TECSUP Store") })
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(padding)
        ) {
            composable("inicio") { PantallaInicio() }
            composable("pedidos") { Text("Mis pedidos") }
            composable("favoritos") { Text("Favoritos") }
            composable("perfil") { Text("Perfil") }
        }
    }
}
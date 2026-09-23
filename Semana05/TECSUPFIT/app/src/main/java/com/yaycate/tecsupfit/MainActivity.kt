package com.yaycate.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yaycate.tecsupfit.ui.theme.TECSUPFITTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TECSUPFITTheme {
                TecsupFitApp()
            }
        }
    }
}

@Composable
fun TecsupFitApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { TecsupBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") {
                InicioScreen(
                    clases = clasesDeEjemplo(),
                    onClaseClick = { claseId -> navController.navigate("detalle/$claseId") }
                )
            }
            composable(
                "detalle/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId") ?: -1
                val clase = clasesDeEjemplo().first { it.id == claseId }
                DetalleClaseScreen(
                    clase = clase,
                    onReservar = { horario -> /* en el próximo paso navegamos a Confirmación */ }
                )
            }
        }

    }}

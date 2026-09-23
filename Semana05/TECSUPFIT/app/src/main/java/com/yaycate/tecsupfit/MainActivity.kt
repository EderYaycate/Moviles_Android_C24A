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
                    onClaseClick = { claseId -> /* en el próximo paso navegamos al detalle */ }
                )
            }
            composable("reservas") {
                Text("Pantalla Reservas (siguiente paso)")
            }
            composable("rutinas") {
                Text("Pantalla Rutinas (siguiente paso)")
            }
            composable("perfil") {
                Text("Pantalla Perfil (siguiente paso)")
            }
        }
    }

}

package com.yaycate.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.clinicasalud.data.DataSource
import com.yaycate.clinicasalud.navigation.Screen
import com.yaycate.clinicasalud.ui.screens.InicioScreen
import com.yaycate.clinicasalud.ui.screens.PerfilMedicoScreen
import com.yaycate.clinicasalud.ui.theme.ClinicaSaludTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicaSaludTheme {
                ClinicaSaludApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ClinicaSaludApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Clínica Salud+") })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) {
                InicioScreen(onMedicoClick = { doctor ->
                    navController.navigate(Screen.Perfil.createRoute(doctor.id))
                })
            }

            composable(
                route = Screen.Perfil.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                val doctor = DataSource.medicos.first { it.id == doctorId }
                PerfilMedicoScreen(
                    doctor = doctor,
                    onAgendarClick = { fecha, hora ->
                        navController.navigate(Screen.Confirmacion.route)
                    }
                )

            }
        }
    }
}
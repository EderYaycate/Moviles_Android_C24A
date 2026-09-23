package com.yaycate.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.tecsupfit.ui.theme.TecsupBottomBar
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

    // Lista mutable inicializada con una reserva de prueba completada
    val reservas = remember {
        mutableStateListOf(
            Reserva(
                id = 1,
                clase = clasesDeEjemplo()[0],
                horarioElegido = "7:00 am",
                estado = "Completada"
            )
        )
    }

    val usuario = remember {
        UsuarioPerfil(nombre = "Eder Yaycate", clasesTomadas = 14, rachaDias = 3)
    }

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
                    usuarioNombre = usuario.nombre.split(" ").firstOrNull() ?: "",
                    onClaseSelected = { claseId -> navController.navigate("detalle/$claseId") }
                )
            }

            composable(
                "detalle/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId") ?: -1
                val clase = clasesDeEjemplo().firstOrNull { it.id == claseId }

                clase?.let {
                    DetalleClaseScreen(
                        clase = it,
                        onBack = { navController.popBackStack() },
                        onReservar = { horario -> navController.navigate("confirmacion/$claseId/$horario") }
                    )
                }
            }

            composable(
                "confirmacion/{claseId}/{horario}",
                arguments = listOf(
                    navArgument("claseId") { type = NavType.IntType },
                    navArgument("horario") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId") ?: -1
                val horario = backStackEntry.arguments?.getString("horario") ?: ""
                val clase = clasesDeEjemplo().firstOrNull { it.id == claseId }

                LaunchedEffect(claseId, horario) {
                    clase?.let {
                        if (reservas.none { r -> r.clase.id == claseId && r.horarioElegido == horario }) {
                            reservas.add(
                                Reserva(
                                    id = reservas.size + 1,
                                    clase = it,
                                    horarioElegido = horario,
                                    estado = "Confirmada"
                                )
                            )
                        }
                    }
                }

                clase?.let {
                    ConfirmacionScreen(
                        clase = it,
                        horario = horario,
                        onVerReservas = {
                            navController.navigate("reservas") {
                                popUpTo("inicio")
                            }
                        }
                    )
                }
            }

            composable("reservas") {
                ReservasScreen(
                    reservas = reservas,
                    onCancelarReserva = { reservaAEliminar ->
                        reservas.remove(reservaAEliminar)
                    }
                )
            }

            composable("rutinas") {
                RutinasScreen()
            }

            composable("perfil") {
                PerfilScreen(usuario = usuario)
            }
        }
    }
}
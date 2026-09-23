package com.yaycate.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.clinicasalud.data.DataSource
import com.yaycate.clinicasalud.model.Cita
import com.yaycate.clinicasalud.navigation.Screen
import com.yaycate.clinicasalud.ui.components.AppDrawerContent
import com.yaycate.clinicasalud.ui.screens.ConfirmacionScreen
import com.yaycate.clinicasalud.ui.screens.InicioScreen
import com.yaycate.clinicasalud.ui.screens.MisCitasScreen
import com.yaycate.clinicasalud.ui.screens.PerfilMedicoScreen
import com.yaycate.clinicasalud.ui.theme.ClinicaSaludTheme
import kotlinx.coroutines.launch

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
    val misCitas = remember { mutableStateListOf<Cita>() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                onInicioClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Inicio.route)
                },
                onMisCitasClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.MisCitas.route)
                },
                onHistorialClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Screen.Historial.route)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Clínica Salud+") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menú")
                        }
                    }
                )
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
                            misCitas.add(Cita(doctor = doctor, fecha = fecha, hora = hora))
                            val fechaCodificada = fecha.replace(" ", "_")
                            val horaCodificada = hora.replace(" ", "_")
                            navController.navigate(
                                Screen.Confirmacion.createRoute(doctorId, fechaCodificada, horaCodificada)
                            )
                        }
                    )
                }

                composable(
                    route = Screen.Confirmacion.route,
                    arguments = listOf(
                        navArgument("doctorId") { type = NavType.IntType },
                        navArgument("fecha") { type = NavType.StringType },
                        navArgument("hora") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                    val fecha = backStackEntry.arguments?.getString("fecha")?.replace("_", " ") ?: ""
                    val hora = backStackEntry.arguments?.getString("hora")?.replace("_", " ") ?: ""
                    val doctor = DataSource.medicos.first { it.id == doctorId }

                    ConfirmacionScreen(
                        doctor = doctor,
                        fecha = fecha,
                        hora = hora,
                        onVolverInicioClick = {
                            navController.navigate(Screen.Inicio.route) {
                                popUpTo(Screen.Inicio.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.MisCitas.route) {
                    MisCitasScreen(
                        citas = misCitas,
                        onCancelarCita = { cita -> misCitas.remove(cita) }
                    )
                }

                composable(Screen.Historial.route) {
                    MisCitasScreen(
                        citas = misCitas.filter { it.estado == "Completada" },
                        onCancelarCita = { cita -> misCitas.remove(cita) }
                    )
                }
            }
        }
    }
}
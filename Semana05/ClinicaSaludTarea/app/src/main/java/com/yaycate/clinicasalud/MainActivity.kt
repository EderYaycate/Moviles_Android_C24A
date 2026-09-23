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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.clinicasalud.data.DataSource
import com.yaycate.clinicasalud.model.Cita
import com.yaycate.clinicasalud.navigation.Screen
import com.yaycate.clinicasalud.ui.components.AppDrawerContent
import com.yaycate.clinicasalud.ui.screens.AgendarCitaScreen
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

@Composable
fun ClinicaSaludApp() {
    val navController = rememberNavController()
    val misCitas = remember { mutableStateListOf<Cita>() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Inicio.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route) {
                        popUpTo(Screen.Inicio.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route
        ) {
            // 1. INICIO
            composable(Screen.Inicio.route) {
                InicioScreen(
                    medicos = DataSource.medicos,
                    especialidades = listOf("Cardiología", "Pediatría", "Dermatología"),
                    onMedicoSelected = { doctor ->
                        navController.navigate(Screen.Perfil.createRoute(doctor.id))
                    },
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            // 2. PERFIL DEL MÉDICO
            composable(
                route = Screen.Perfil.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                val doctor = DataSource.medicos.first { it.id == doctorId }
                PerfilMedicoScreen(
                    doctor = doctor,
                    onAgendarClick = {
                        navController.navigate("agendar/${doctor.id}")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // 3. AGENDAR CITA
            composable(
                route = "agendar/{doctorId}",
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStackEntry ->
                val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
                val doctor = DataSource.medicos.first { it.id == doctorId }

                AgendarCitaScreen(
                    onConfirmar = { fecha, hora ->
                        val nuevaCita = Cita(
                            doctor = doctor,
                            fecha = fecha,
                            hora = hora,
                            estado = "Confirmada"
                        )
                        misCitas.add(nuevaCita)

                        val fechaCod = fecha.replace(" ", "_")
                        val horaCod = hora.replace(" ", "_")
                        navController.navigate(
                            Screen.Confirmacion.createRoute(doctorId, fechaCod, horaCod)
                        )
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            // 4. CONFIRMACIÓN
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
                    doctorNombre = doctor.nombre,
                    fecha = fecha,
                    hora = hora,
                    onVerMisCitas = {
                        navController.navigate(Screen.MisCitas.route) {
                            popUpTo(Screen.Inicio.route)
                        }
                    }
                )
            }

            // 5. MIS CITAS (Con opción de cancelar)
            composable(Screen.MisCitas.route) {
                MisCitasScreen(
                    citas = misCitas,
                    onCancelarCita = { cita -> misCitas.remove(cita) }
                )
            }

            // 6. HISTORIAL
            composable(Screen.Historial.route) {
                MisCitasScreen(
                    citas = misCitas.filter { it.estado == "Completada" },
                    onCancelarCita = { cita -> misCitas.remove(cita) }
                )
            }
        }
    }
}
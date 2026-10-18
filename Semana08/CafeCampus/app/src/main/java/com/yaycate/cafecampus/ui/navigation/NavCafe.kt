package com.yaycate.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yaycate.cafecampus.data.local.CafeDatabase
import com.yaycate.cafecampus.data.prefs.PreferenciasStore
import com.yaycate.cafecampus.data.repository.CafeRepository
import com.yaycate.cafecampus.data.repository.CafeRepositoryRoom
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.cliente.LoginScreen
import com.yaycate.cafecampus.ui.cliente.SplashScreen
import com.yaycate.cafecampus.viewmodel.CafeViewModelFactory
import com.yaycate.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe() {
    val context = LocalContext.current
    val nav = rememberNavController()

    val repo: CafeRepository = remember { CafeRepositoryRoom(CafeDatabase.obtener(context)) }
    val prefs = remember { PreferenciasStore(context) }
    val factory = remember { CafeViewModelFactory(repo, prefs) }
    val sesionVm: SesionViewModel = viewModel(factory = factory)

    NavHost(navController = nav, startDestination = Ruta.Splash.ruta) {

        composable(Ruta.Splash.ruta) {
            SplashScreen(prefs = prefs) { rol ->
                val destino = when (rol) {
                    null -> Ruta.Login.ruta
                    Rol.ADMIN -> Ruta.Panel.ruta
                    Rol.CLIENTE -> Ruta.Menu.ruta
                }
                nav.navigate(destino) {
                    popUpTo(Ruta.Splash.ruta) { inclusive = true }
                }
            }
        }

        composable(Ruta.Login.ruta) {
            LoginScreen(vm = sesionVm) { rol ->
                val destino = if (rol == Rol.ADMIN) Ruta.Panel.ruta else Ruta.Menu.ruta
                nav.navigate(destino) {
                    popUpTo(Ruta.Login.ruta) { inclusive = true }
                }
            }
        }

        // Provisionales: se reemplazan en los pasos del menú y del panel
        composable(Ruta.Menu.ruta) { PantallaProvisional("Menú (pendiente)") }
        composable(Ruta.Panel.ruta) { PantallaProvisional("Panel (pendiente)") }
    }
}

@Composable
private fun PantallaProvisional(titulo: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(titulo, style = MaterialTheme.typography.titleMedium)
    }
}
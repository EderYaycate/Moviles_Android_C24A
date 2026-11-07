package com.yaycate.animetec.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.animetec.AppContainer
import com.yaycate.animetec.ui.DetalleViewModelFactory
import com.yaycate.animetec.ui.ViewModelFactory
import com.yaycate.animetec.ui.detalle.DetalleRoute
import com.yaycate.animetec.ui.detalle.DetalleViewModel
import com.yaycate.animetec.ui.favoritos.FavoritosRoute
import com.yaycate.animetec.ui.favoritos.FavoritosViewModel
import com.yaycate.animetec.ui.lista.ListaRoute
import com.yaycate.animetec.ui.lista.ListaViewModel

private const val RUTA_LISTA = "lista"
private const val RUTA_FAVORITOS = "favoritos"

@Composable
fun AppNav(container: AppContainer) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = RUTA_LISTA) {

        composable(RUTA_LISTA) {
            val vm: ListaViewModel = viewModel(factory = ViewModelFactory(container))
            ConBarraInferior(nav, actual = RUTA_LISTA) { padding ->
                ListaRoute(
                    viewModel = vm,
                    onItemClick = { id -> nav.navigate("detalle/$id") },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(RUTA_FAVORITOS) {
            val vm: FavoritosViewModel = viewModel(factory = ViewModelFactory(container))
            ConBarraInferior(nav, actual = RUTA_FAVORITOS) { padding ->
                FavoritosRoute(
                    viewModel = vm,
                    onItemClick = { id -> nav.navigate("detalle/$id") },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments!!.getInt("id")
            val vm: DetalleViewModel = viewModel(
                key = "detalle$id",
                factory = DetalleViewModelFactory(container, id)
            )
            DetalleRoute(viewModel = vm, onVolver = { nav.popBackStack() })
        }
    }
}

@Composable
private fun ConBarraInferior(
    nav: NavController,
    actual: String,
    contenido: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = actual == RUTA_LISTA,
                    onClick = { irA(nav, RUTA_LISTA) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Lista") },
                    label = { Text("Lista") }
                )
                NavigationBarItem(
                    selected = actual == RUTA_FAVORITOS,
                    onClick = { irA(nav, RUTA_FAVORITOS) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
            }
        }
    ) { padding -> contenido(padding) }
}

private fun irA(nav: NavController, ruta: String) {
    nav.navigate(ruta) {
        popUpTo(RUTA_LISTA) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
package com.yaycate.animetec.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.yaycate.animetec.ui.lista.ListaRoute
import com.yaycate.animetec.ui.lista.ListaViewModel

@Composable
fun AppNav(container: AppContainer) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "lista") {

        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = ViewModelFactory(container))
            Scaffold { padding ->
                ListaRoute(
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
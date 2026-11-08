package com.yaycate.animetec.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.animetec.AppContainer
import com.yaycate.animetec.domain.model.Rol
import com.yaycate.animetec.ui.DetalleViewModelFactory
import com.yaycate.animetec.ui.ViewModelFactory
import com.yaycate.animetec.ui.admin.AdminRoute
import com.yaycate.animetec.ui.admin.AdminViewModel
import com.yaycate.animetec.ui.detalle.DetalleRoute
import com.yaycate.animetec.ui.detalle.DetalleViewModel
import com.yaycate.animetec.ui.favoritos.FavoritosRoute
import com.yaycate.animetec.ui.favoritos.FavoritosViewModel
import com.yaycate.animetec.ui.lista.ListaRoute
import com.yaycate.animetec.ui.lista.ListaViewModel
import com.yaycate.animetec.ui.login.LoginRoute
import com.yaycate.animetec.ui.login.LoginViewModel
import com.yaycate.animetec.ui.login.SesionViewModel
import com.yaycate.animetec.ui.novedades.NovedadesRoute
import com.yaycate.animetec.ui.novedades.NovedadesViewModel
import com.yaycate.animetec.ui.perfil.PerfilRoute
import com.yaycate.animetec.ui.perfil.PerfilViewModel
import com.yaycate.animetec.ui.registro.RegistroRoute
import com.yaycate.animetec.ui.registro.RegistroViewModel

private const val RUTA_LOGIN = "login"
private const val RUTA_REGISTRO = "registro"
private const val RUTA_PERFIL = "perfil"
private const val RUTA_NOVEDADES = "novedades"
private const val RUTA_LISTA = "lista"
private const val RUTA_FAVORITOS = "favoritos"
private const val RUTA_ADMIN = "admin"

@Composable
fun AppNav(container: AppContainer) {
    val nav = rememberNavController()
    val sesionVm: SesionViewModel = viewModel(factory = ViewModelFactory(container))
    val sesion by sesionVm.sesion.collectAsStateWithLifecycle()
    val esAdmin = sesion?.rol == Rol.ADMIN
    val uid = sesion?.uid ?: "guest"

    val novedadesVm: NovedadesViewModel = viewModel(
        key = "novedades-$uid",
        factory = ViewModelFactory(container)
    )
    val novedadesUiState by novedadesVm.uiState.collectAsStateWithLifecycle()
    val sinLeer = novedadesUiState.sinLeer

    NavHost(
        navController = nav,
        startDestination = RUTA_LOGIN,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {

        composable(RUTA_LOGIN) {
            val vm: LoginViewModel = viewModel(factory = ViewModelFactory(container))
            LoginRoute(
                viewModel = vm,
                onSesionIniciada = { s ->
                    sesionVm.establecer(s)
                    nav.navigate(RUTA_LISTA) {
                        popUpTo(nav.graph.id) { inclusive = true }
                    }
                },
                onIrARegistro = { nav.navigate(RUTA_REGISTRO) }
            )
        }

        composable(RUTA_REGISTRO) {
            val vm: RegistroViewModel = viewModel(factory = ViewModelFactory(container))
            RegistroRoute(
                viewModel = vm,
                onSesionIniciada = { s ->
                    sesionVm.establecer(s)
                    nav.navigate(RUTA_LISTA) {
                        popUpTo(nav.graph.id) { inclusive = true }
                    }
                },
                onVolverLogin = { nav.popBackStack() }
            )
        }

        composable(RUTA_PERFIL) {
            val vm: PerfilViewModel = viewModel(factory = ViewModelFactory(container))
            PerfilRoute(
                viewModel = vm,
                onVolver = { nav.popBackStack() },
                onCerrarSesion = {
                    sesionVm.cerrarSesion()
                    nav.navigate(RUTA_LOGIN) {
                        popUpTo(nav.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(RUTA_NOVEDADES) {
            NovedadesRoute(
                viewModel = novedadesVm,
                onVolver = { nav.popBackStack() },
                onVerAnime = { animeId -> nav.navigate("detalle/$animeId") }
            )
        }

        composable(
            route = RUTA_LISTA,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            val vm: ListaViewModel = viewModel(factory = ViewModelFactory(container))
            ConBarraInferior(nav, RUTA_LISTA, esAdmin, sinLeer) { padding ->
                ListaRoute(
                    viewModel = vm,
                    onItemClick = { id -> nav.navigate("detalle/$id") },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = RUTA_FAVORITOS,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            val vm: FavoritosViewModel = viewModel(factory = ViewModelFactory(container))
            ConBarraInferior(nav, RUTA_FAVORITOS, esAdmin, sinLeer) { padding ->
                FavoritosRoute(
                    viewModel = vm,
                    onItemClick = { id -> nav.navigate("detalle/$id") },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = RUTA_ADMIN,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            if (esAdmin) {
                val vm: AdminViewModel = viewModel(factory = ViewModelFactory(container))
                ConBarraInferior(nav, RUTA_ADMIN, esAdmin, sinLeer) { padding ->
                    AdminRoute(viewModel = vm, modifier = Modifier.padding(padding))
                }
            } else {
                // Un usuario sin rol admin nunca ve esta pantalla
                LaunchedEffect(Unit) { irA(nav, RUTA_LISTA) }
            }
        }

        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConBarraInferior(
    nav: NavController,
    actual: String,
    esAdmin: Boolean,
    sinLeer: Int,
    contenido: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AnimeTec") },
                actions = {
                    IconButton(onClick = { nav.navigate(RUTA_NOVEDADES) }) {
                        BadgedBox(
                            badge = {
                                Column {
                                    AnimatedVisibility(
                                        visible = sinLeer > 0,
                                        enter = scaleIn() + fadeIn(),
                                        exit = scaleOut() + fadeOut()
                                    ) {
                                        Badge {
                                            Text(sinLeer.toString())
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Novedades")
                        }
                    }
                    IconButton(onClick = { nav.navigate(RUTA_PERFIL) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        },
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
                if (esAdmin) {
                    NavigationBarItem(
                        selected = actual == RUTA_ADMIN,
                        onClick = { irA(nav, RUTA_ADMIN) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Panel Admin") },
                        label = { Text("Admin") }
                    )
                }
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

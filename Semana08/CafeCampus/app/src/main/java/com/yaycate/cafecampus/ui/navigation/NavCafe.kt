package com.yaycate.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.cafecampus.data.local.CafeDatabase
import com.yaycate.cafecampus.data.prefs.PreferenciasStore
import com.yaycate.cafecampus.data.repository.CafeRepository
import com.yaycate.cafecampus.data.repository.CafeRepositoryRoom
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.yaycate.cafecampus.ui.cliente.DetalleProductoScreen
import com.yaycate.cafecampus.ui.cliente.DetallePedidoAdminScreen
import com.yaycate.cafecampus.ui.cliente.FormularioProductoScreen
import com.yaycate.cafecampus.ui.cliente.LoginScreen
import com.yaycate.cafecampus.ui.cliente.MenuAdminScreen
import com.yaycate.cafecampus.ui.cliente.MenuScreen
import com.yaycate.cafecampus.ui.cliente.PanelScreen
import com.yaycate.cafecampus.ui.cliente.PedidosAdminScreen
import com.yaycate.cafecampus.ui.cliente.PedidosScreen
import com.yaycate.cafecampus.ui.cliente.SeguimientoPedidoScreen
import com.yaycate.cafecampus.ui.cliente.SplashScreen
import com.yaycate.cafecampus.viewmodel.CafeViewModelFactory
import com.yaycate.cafecampus.viewmodel.MenuAdminViewModel
import com.yaycate.cafecampus.viewmodel.MenuViewModel
import com.yaycate.cafecampus.viewmodel.PanelViewModel
import com.yaycate.cafecampus.viewmodel.PedidosAdminViewModel
import com.yaycate.cafecampus.viewmodel.PedidosClienteViewModel
import com.yaycate.cafecampus.viewmodel.SesionViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember as recordar

@Composable
fun NavCafe() {
    val context = LocalContext.current
    val nav = rememberNavController()

    val repo: CafeRepository = remember { CafeRepositoryRoom(CafeDatabase.obtener(context)) }
    val prefs = remember { PreferenciasStore(context) }
    val factory = remember { CafeViewModelFactory(repo, prefs) }

    // Id del cliente guardado en DataStore; sirve aunque se haya reabierto la app
    val clienteId by prefs.clienteId.collectAsState(initial = -1)

    // Instancias compartidas entre pantallas de cada flujo
    val sesionVm: SesionViewModel = viewModel(factory = factory)
    val menuVm: MenuViewModel = viewModel(factory = factory)
    val pedidosVm: PedidosClienteViewModel = viewModel(factory = factory)
    val panelVm: PanelViewModel = viewModel(factory = factory)
    val pedidosAdminVm: PedidosAdminViewModel = viewModel(factory = factory)
    val menuAdminVm: MenuAdminViewModel = viewModel(factory = factory)

    // Cierra la sesión y vuelve al login borrando todo el historial de navegación
    val cerrarSesion: () -> Unit = {
        sesionVm.cerrarSesion()
        nav.navigate(Ruta.Login.ruta) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

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

        // ----- Cliente -----

        composable(Ruta.Menu.ruta) {
            MenuScreen(
                vm = menuVm,
                onProductoClick = { id -> nav.navigate("detalle/$id") },
                onVerCarrito = { nav.navigate(Ruta.Confirmar.ruta) },
                onVerPedidos = { nav.navigate(Ruta.Pedidos.ruta) },
                onCerrarSesion = cerrarSesion
            )
        }

        composable(
            route = Ruta.Detalle.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("productoId") ?: -1
            DetalleProductoScreen(
                vm = menuVm,
                productoId = id,
                onAgregado = { nav.popBackStack() },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(Ruta.Confirmar.ruta) {
            ConfirmarPedidoScreen(
                vm = menuVm,
                clienteId = clienteId,
                onConfirmado = { nav.popBackStack(Ruta.Menu.ruta, inclusive = false) },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(Ruta.Pedidos.ruta) {
            PedidosScreen(
                vm = pedidosVm,
                clienteId = clienteId,
                onPedidoClick = { id -> nav.navigate("seguimiento/$id") },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(
            route = Ruta.Seguimiento.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("pedidoId") ?: -1
            SeguimientoPedidoScreen(
                vm = pedidosVm,
                pedidoId = id,
                onVolver = { nav.popBackStack() }
            )
        }

        // ----- Administrador -----

        composable(Ruta.Panel.ruta) {
            PanelScreen(
                vm = panelVm,
                onVerPedidos = { nav.navigate(Ruta.PedidosAdmin.ruta) },
                onGestionarMenu = { nav.navigate(Ruta.MenuAdmin.ruta) },
                onCerrarSesion = cerrarSesion
            )
        }

        composable(Ruta.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                vm = pedidosAdminVm,
                onPedidoClick = { id -> nav.navigate("detalle_pedido_admin/$id") },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(
            route = Ruta.DetallePedidoAdmin.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("pedidoId") ?: -1
            DetallePedidoAdminScreen(
                vm = pedidosAdminVm,
                pedidoId = id,
                onVolver = { nav.popBackStack() }
            )
        }

        composable(Ruta.MenuAdmin.ruta) {
            MenuAdminScreen(
                vm = menuAdminVm,
                onEditar = { id -> nav.navigate("formulario_producto?productoId=$id") },
                onNuevo = { nav.navigate("formulario_producto?productoId=-1") },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(
            route = Ruta.FormularioProducto.ruta,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entrada ->
            val id = entrada.arguments?.getInt("productoId") ?: -1
            FormularioProductoScreen(
                vm = menuAdminVm,
                productoId = id,
                onGuardado = { nav.popBackStack() },
                onVolver = { nav.popBackStack() }
            )
        }
    }
}
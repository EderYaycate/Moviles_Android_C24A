package com.yaycate.mibodega

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        composable(Rutas.Login.ruta) {
            PantallaLogin(
                onLoginExitoso = { navController.navigate(Rutas.Inicio.ruta) },
                onIrARegistro = { navController.navigate(Rutas.CrearCuenta.ruta) }
            )
        }

        composable(Rutas.CrearCuenta.ruta) {
            PantallaCrearCuenta(
                onCuentaCreada = { navController.navigate(Rutas.Inicio.ruta) }
            )
        }

        composable(Rutas.Inicio.ruta) {
            PantallaInicio(
                onProductoClick = { id ->
                    navController.navigate(Rutas.DetalleProducto.crearRuta(id))
                }
            )
        }

        composable(
            route = Rutas.DetalleProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("productoId") ?: 0
            PantallaDetalleProducto(
                productoId = id,
                onAgregarAlCarrito = { navController.navigate(Rutas.Carrito.ruta) },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Carrito.ruta) {
            PantallaCarrito(
                onContinuarCompra = { navController.navigate(Rutas.DatosEntrega.ruta) }
            )
        }

        composable(Rutas.DatosEntrega.ruta) {
            PantallaDatosEntrega(
                onConfirmarPedido = { idPedido, total ->
                    navController.navigate(Rutas.Confirmacion.crearRuta(idPedido, total))
                }
            )
        }

        composable(
            route = Rutas.Confirmacion.ruta,
            arguments = listOf(
                navArgument("idPedido") { type = NavType.StringType },
                navArgument("total") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idPedido = backStackEntry.arguments?.getString("idPedido") ?: ""
            val total = backStackEntry.arguments?.getString("total") ?: ""
            PantallaConfirmacion(
                idPedido = idPedido,
                total = total,
                onVolverInicio = {
                    navController.navigate(Rutas.Inicio.ruta) {
                        popUpTo(Rutas.Inicio.ruta) { inclusive = true }
                    }
                }
            )
        }
    }
}
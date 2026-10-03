package com.yaycate.mibodega.ui.cliente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yaycate.mibodega.ui.cliente.modelo.DatosFake
import com.yaycate.mibodega.ui.cliente.modelo.ItemCarrito
import com.yaycate.mibodega.ui.cliente.modelo.Producto
import com.yaycate.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.yaycate.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.yaycate.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.yaycate.mibodega.ui.cliente.screens.detalle.DetalleScreen
import com.yaycate.mibodega.ui.cliente.screens.entrega.EntregaScreen
import com.yaycate.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.yaycate.mibodega.ui.cliente.screens.registro.RegistroScreen

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val PEDIDOS = "pedidos"
    const val PERFIL = "perfil"
    const val DETALLE = "detalle"
    const val CARRITO = "carrito"
    const val ENTREGA = "entrega"
    const val CONFIRMACION = "confirmacion"
}

private data class OpcionBarra(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

private val opcionesBarra = listOf(
    OpcionBarra(Rutas.INICIO, "Inicio", Icons.Default.Home),
    OpcionBarra(Rutas.CATEGORIAS, "Categorías", Icons.AutoMirrored.Filled.List),
    OpcionBarra(Rutas.PEDIDOS, "Pedidos", Icons.Default.ShoppingCart),
    OpcionBarra(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

private data class DatosUsuario(
    val nombre: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val referencia: String = ""
)

private fun sumarProducto(
    lista: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val existe = lista.any { it.producto.id == producto.id }
    return if (existe) {
        lista.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        lista + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}

private fun restarProducto(lista: List<ItemCarrito>, producto: Producto): List<ItemCarrito> =
    lista.mapNotNull {
        if (it.producto.id == producto.id) {
            if (it.cantidad > 1) it.copy(cantidad = it.cantidad - 1) else null
        } else it
    }

private fun quitarProducto(lista: List<ItemCarrito>, producto: Producto): List<ItemCarrito> =
    lista.filterNot { it.producto.id == producto.id }

@Composable
private fun PantallaMarcador(titulo: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(titulo)
    }
}

@Composable
fun ClienteApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    var carrito by remember { mutableStateOf(listOf<ItemCarrito>()) }
    var usuario by remember { mutableStateOf(DatosUsuario()) }
    var direccionPedido by remember { mutableStateOf("") }
    var referenciaPedido by remember { mutableStateOf("") }

    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val delivery = if (carrito.isNotEmpty()) 4.0 else 0.0
    val totalCalculado = subtotal + delivery

    Scaffold(
        bottomBar = {
            if (opcionesBarra.any { it.ruta == rutaActual }) {
                NavigationBar(containerColor = Color.White) {
                    opcionesBarra.forEach { opcion ->
                        NavigationBarItem(
                            selected = rutaActual == opcion.ruta,
                            onClick = {
                                navController.navigate(opcion.ruta) {
                                    popUpTo(Rutas.INICIO) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(opcion.icono, contentDescription = opcion.titulo) },
                            label = { Text(opcion.titulo) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VerdeBodega,
                                selectedTextColor = VerdeBodega,
                                indicatorColor = VerdeClaro
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.BIENVENIDA,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                    onIniciarSesion = { navController.navigate(Rutas.INICIO) },
                    onTerminos = { }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onCrearCuenta = { nombre, telefono, direccion, referencia ->
                        usuario = DatosUsuario(nombre, telefono, direccion, referencia)
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                        }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Rutas.INICIO) {
                InicioScreen(
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    onProductoClick = { producto ->
                        navController.navigate("${Rutas.DETALLE}/${producto.id}")
                    },
                    onAgregarAlCarrito = { producto ->
                        carrito = sumarProducto(carrito, producto, 1)
                    },
                    onIrAlCarrito = { navController.navigate(Rutas.CARRITO) }
                )
            }

            composable(Rutas.CATEGORIAS) { PantallaMarcador("Categorías") }
            composable(Rutas.PEDIDOS) { PantallaMarcador("Mis pedidos") }
            composable(Rutas.PERFIL) { PantallaMarcador("Perfil") }

            composable(
                route = "${Rutas.DETALLE}/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { entrada ->
                val productoId = entrada.arguments?.getInt("productoId")
                val producto = DatosFake.productos.firstOrNull { it.id == productoId }
                if (producto != null) {
                    DetalleScreen(
                        producto = producto,
                        onVolver = { navController.popBackStack() },
                        onAgregarAlCarrito = { cantidad ->
                            carrito = sumarProducto(carrito, producto, cantidad)
                            navController.navigate(Rutas.CARRITO)
                        }
                    )
                }
            }

            composable(Rutas.CARRITO) {
                CarritoScreen(
                    carrito = carrito,
                    subtotal = subtotal,
                    delivery = delivery,
                    total = totalCalculado,
                    onIncrementar = { item ->
                        carrito = sumarProducto(carrito, item.producto, 1)
                    },
                    onDecrementar = { item ->
                        carrito = restarProducto(carrito, item.producto)
                    },
                    onEliminar = { item ->
                        carrito = quitarProducto(carrito, item.producto)
                    },
                    onContinuarPedido = { navController.navigate(Rutas.ENTREGA) },
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Rutas.ENTREGA) {
                EntregaScreen(
                    total = totalCalculado,
                    nombreInicial = usuario.nombre,
                    telefonoInicial = usuario.telefono,
                    direccionInicial = usuario.direccion,
                    referenciaInicial = usuario.referencia,
                    onConfirmarPedido = { direccion, referencia ->
                        direccionPedido = direccion
                        referenciaPedido = referencia
                        navController.navigate(Rutas.CONFIRMACION) {
                            popUpTo(Rutas.INICIO)
                        }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Rutas.CONFIRMACION) {
                ConfirmacionScreen(
                    idPedido = "#1024",
                    total = totalCalculado,
                    direccion = direccionPedido,
                    referencia = referenciaPedido,
                    onVolverInicio = {
                        carrito = emptyList()
                        navController.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
package com.yaycate.mibodega

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object CrearCuenta : Rutas("crear_cuenta")
    object Inicio : Rutas("inicio")
    object Carrito : Rutas("carrito")
    object DatosEntrega : Rutas("datos_entrega")
    object Confirmacion : Rutas("confirmacion")

    object DetalleProducto : Rutas("detalle/{productoId}") {
        fun crearRuta(productoId: Int) = "detalle/$productoId"
    }
}
package com.yaycate.cafecampus.ui.navigation

sealed class Ruta(val ruta: String) {
    object Splash : Ruta("splash")
    object Login : Ruta("login")
    object Registro : Ruta("registro")

    // Cliente
    object Menu : Ruta("menu")
    object Pedidos : Ruta("pedidos")
    object Perfil : Ruta("perfil")
    object Confirmar : Ruta("confirmar")
    object Detalle : Ruta("detalle/{productoId}") {
        fun crear(productoId: Int) = "detalle/$productoId"
    }
    object Seguimiento : Ruta("seguimiento/{pedidoId}") {
        fun crear(pedidoId: Int) = "seguimiento/$pedidoId"
    }

    // Administrador
    object Panel : Ruta("panel")
    object PedidosAdmin : Ruta("pedidos_admin")
    object DetallePedidoAdmin : Ruta("detalle_pedido_admin/{pedidoId}") {
        fun crear(pedidoId: Int) = "detalle_pedido_admin/$pedidoId"
    }
    object MenuAdmin : Ruta("menu_admin")
    object FormularioProducto : Ruta("formulario_producto?productoId={productoId}") {
        fun crear(productoId: Int? = null) =
            if (productoId == null) "formulario_producto?productoId=-1"
            else "formulario_producto?productoId=$productoId"
    }
    object Ajustes : Ruta("ajustes")
}
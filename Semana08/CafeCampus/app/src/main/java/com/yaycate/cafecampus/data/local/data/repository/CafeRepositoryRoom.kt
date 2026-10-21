package com.yaycate.cafecampus.data.repository

import com.yaycate.cafecampus.data.local.CafeDatabase
import com.yaycate.cafecampus.data.local.entity.DetallePedido
import com.yaycate.cafecampus.data.local.entity.Pedido
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.data.local.entity.Usuario
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.model.ItemCarrito
import com.yaycate.cafecampus.model.Rol
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(private val db: CafeDatabase) : CafeRepository {

    // Usuarios
    override suspend fun login(correo: String, clave: String): Usuario? =
        db.usuarioDao().login(correo, clave)

    override suspend fun registrarCliente(
        nombre: String,
        correo: String,
        clave: String
    ): Result<Long> {
        val correoLimpio = correo.trim().lowercase()
        val existe = db.usuarioDao().buscarPorCorreo(correoLimpio)
        if (existe != null) {
            return Result.failure(IllegalArgumentException("Ese correo ya está registrado"))
        }
        val usuario = Usuario(
            nombre = nombre.trim(),
            correo = correoLimpio,
            clave = clave.trim(),
            rol = Rol.CLIENTE.name
        )
        val id = db.usuarioDao().insertar(usuario)
        return Result.success(id)
    }

    // Productos
    override fun observarProductos(): Flow<List<Producto>> =
        db.productoDao().observarTodos()

    override suspend fun guardarProducto(p: Producto) {
        if (p.id == 0) db.productoDao().insertar(p) else db.productoDao().actualizar(p)
    }

    override suspend fun eliminarProducto(p: Producto) =
        db.productoDao().eliminar(p)

    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) =
        db.productoDao().cambiarDisponibilidad(id, disponible)

    // Pedidos
    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        val detalles = items.map {
            DetallePedido(
                pedidoId = 0,
                productoId = it.productoId,
                nombreProducto = it.nombre,
                cantidad = it.cantidad,
                precioUnitario = it.precio
            )
        }
        val recargo = if (pago.equals("Yape", ignoreCase = true) || pago.equals("Plin", ignoreCase = true)) 1.0 else 0.0
        val total = items.sumOf { it.precio * it.cantidad } + recargo
        val pedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = total
        )
        return db.pedidoDao().crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> =
        db.pedidoDao().observarPorCliente(clienteId)

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> =
        db.pedidoDao().observarPorEstado(estado.name)

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> =
        db.pedidoDao().observarUno(id)

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) =
        db.pedidoDao().cambiarEstado(pedidoId, estado.name)

    // Panel
    override fun observarVentasHoy(inicioDia: Long): Flow<Double> =
        db.pedidoDao().ventasDesde(inicioDia)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> =
        db.pedidoDao().cantidadPorEstado(estado.name)
}
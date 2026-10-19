package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.data.repository.CafeRepository
import com.yaycate.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {

    // Lista de productos que se actualiza sola cuando cambia la base de datos
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    fun cambiarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    val productosFiltrados: StateFlow<List<Producto>> = combine(productos, _busqueda) { lista, query ->
        val queryLimpia = query.trim()
        if (queryLimpia.isBlank()) {
            lista
        } else {
            lista.filter { it.nombre.contains(queryLimpia, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // Carrito en memoria: vive mientras la app esté abierta
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    val totalCarrito: StateFlow<Double> = _carrito
        .map { items -> items.sumOf { it.precio * it.cantidad } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0
        )

    // Agrega el producto; si ya estaba en el carrito, suma la cantidad
    fun agregar(producto: Producto, cantidad: Int, nota: String = "") {
        if (cantidad <= 0) return
        val actual = _carrito.value.toMutableList()
        val indice = actual.indexOfFirst { it.productoId == producto.id }
        if (indice >= 0) {
            val item = actual[indice]
            actual[indice] = item.copy(
                cantidad = item.cantidad + cantidad,
                nota = nota.ifBlank { item.nota }
            )
        } else {
            actual.add(
                ItemCarrito(
                    productoId = producto.id,
                    nombre = producto.nombre,
                    precio = producto.precio,
                    cantidad = cantidad,
                    nota = nota
                )
            )
        }
        _carrito.value = actual
    }

    // Repite un pedido anterior agregando al carrito solo los productos disponibles con precio actual
    fun repetirPedido(
        pedido: PedidoConDetalle,
        onResultado: (noDisponibles: List<String>) -> Unit
    ) {
        viewModelScope.launch {
            val listaProductos = repo.observarProductos().first()
            val noDisponibles = mutableListOf<String>()

            pedido.detalles.forEach { detalle ->
                val prodActual = listaProductos.find { it.id == detalle.productoId }
                if (prodActual != null && prodActual.disponible) {
                    agregar(
                        producto = prodActual,
                        cantidad = detalle.cantidad,
                        nota = pedido.pedido.nota
                    )
                } else {
                    noDisponibles.add(detalle.nombreProducto)
                }
            }

            onResultado(noDisponibles)
        }
    }

    // Cambia la cantidad; con 0 o menos quita el producto
    fun cambiarCantidad(productoId: Int, cantidad: Int) {
        _carrito.value = if (cantidad <= 0) {
            _carrito.value.filterNot { it.productoId == productoId }
        } else {
            _carrito.value.map { item ->
                if (item.productoId == productoId) item.copy(cantidad = cantidad) else item
            }
        }
    }

    fun quitar(productoId: Int) = cambiarCantidad(productoId, 0)

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    // Guarda el pedido con lo que hay en el carrito, vacía el carrito y devuelve el id del pedido
    suspend fun confirmarPedido(
        clienteId: Int,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        val id = repo.crearPedido(clienteId, _carrito.value, horaRecojo, pago, nota)
        _carrito.value = emptyList()
        return id
    }
}
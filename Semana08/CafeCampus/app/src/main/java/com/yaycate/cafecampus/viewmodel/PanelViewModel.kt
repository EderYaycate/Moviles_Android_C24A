package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.data.repository.CafeRepository
import com.yaycate.cafecampus.model.EstadoPedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class CategoriaHora {
    MAS_VENDIDA,
    MENOS_VENDIDA,
    INTERMEDIA,
    SIN_VENTAS
}

data class VentaHora(
    val hora: Int,
    val cantidad: Int,
    val categoria: CategoriaHora = CategoriaHora.SIN_VENTAS
)

class PanelViewModel(private val repo: CafeRepository) : ViewModel() {

    private val inicio = inicioDelDia()

    // Ventas desde las 00:00 de hoy; excluye pedidos cancelados
    val ventasHoy: StateFlow<Double> = repo.observarVentasHoy(inicio)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    // Cantidad de pedidos en cada estado
    val recibidos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val preparando: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.PREPARANDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val listos: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    // Pedidos recién llegados, listos para pasar a Preparando
    val nuevos: StateFlow<List<PedidoConDetalle>> = repo.observarPedidosPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Pedidos de hoy que no están cancelados (une los flujos de cada estado)
    private val pedidosHoy: Flow<List<PedidoConDetalle>> = combine(
        EstadoPedido.entries
            .filter { it != EstadoPedido.CANCELADO }
            .map { repo.observarPedidosPorEstado(it) }
    ) { listas ->
        listas.flatMap { it }.filter { it.pedido.fecha >= inicio }
    }

    val cantidadHoy: StateFlow<Int> = pedidosHoy
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    // Pedidos agrupados por hora, desde la primera hasta la última hora con pedidos (mínimo 8h a 13h)
    val pedidosPorHora: StateFlow<List<VentaHora>> = pedidosHoy
        .map { lista ->
            val horasConPedidos = lista.map { horaDe(it.pedido.fecha) }
            val minHora = (horasConPedidos.minOrNull() ?: 8).coerceAtMost(8)
            val maxHora = (horasConPedidos.maxOrNull() ?: 13).coerceAtLeast(13)
            val conteo = lista.groupingBy { horaDe(it.pedido.fecha) }.eachCount()

            val horasConVentas = (minHora..maxHora).mapNotNull { h ->
                val cant = conteo[h] ?: 0
                if (cant > 0) cant else null
            }

            val maxVentas = horasConVentas.maxOrNull() ?: 0
            val minVentas = horasConVentas.minOrNull() ?: 0

            (minHora..maxHora).map { h ->
                val cant = conteo[h] ?: 0
                val cat = when {
                    cant == 0 -> CategoriaHora.SIN_VENTAS
                    maxVentas == minVentas -> CategoriaHora.MAS_VENDIDA
                    cant == maxVentas -> CategoriaHora.MAS_VENDIDA
                    cant == minVentas -> CategoriaHora.MENOS_VENDIDA
                    else -> CategoriaHora.INTERMEDIA
                }
                VentaHora(hora = h, cantidad = cant, categoria = cat)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            (8..13).map { VentaHora(it, 0, CategoriaHora.SIN_VENTAS) }
        )

    fun preparar(pedidoId: Int) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, EstadoPedido.PREPARANDO)
        }
    }

    private fun horaDe(millis: Long): Int =
        Calendar.getInstance().apply { timeInMillis = millis }.get(Calendar.HOUR_OF_DAY)

    private fun inicioDelDia(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
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

data class VentaHora(val hora: Int, val cantidad: Int)

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

    // Pedidos agrupados por hora, de 8h a 13h
    val pedidosPorHora: StateFlow<List<VentaHora>> = pedidosHoy
        .map { lista ->
            val conteo = lista.groupingBy { horaDe(it.pedido.fecha) }.eachCount()
            HORAS.map { h -> VentaHora(h, conteo[h] ?: 0) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HORAS.map { VentaHora(it, 0) }
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

    companion object {
        private val HORAS = (8..13).toList()
    }
}
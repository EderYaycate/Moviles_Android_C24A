package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.data.repository.CafeRepository
import com.yaycate.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    private val estadoFiltro = MutableStateFlow(EstadoPedido.RECIBIDO)
    val estado: StateFlow<EstadoPedido> = estadoFiltro.asStateFlow()

    // Se actualiza sola al cambiar el filtro o el estado de un pedido
    @OptIn(ExperimentalCoroutinesApi::class)
    val pedidos: StateFlow<List<PedidoConDetalle>> = estadoFiltro
        .flatMapLatest { repo.observarPedidosPorEstado(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun filtrar(nuevo: EstadoPedido) {
        estadoFiltro.value = nuevo
    }

    fun cambiarEstado(pedidoId: Int, nuevo: EstadoPedido) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, nuevo)
        }
    }
}
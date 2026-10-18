package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class PedidosClienteViewModel(private val repo: CafeRepository) : ViewModel() {

    private val clienteId = MutableStateFlow<Int?>(null)

    // Se actualiza sola cuando cambian los pedidos del cliente cargado
    @OptIn(ExperimentalCoroutinesApi::class)
    val pedidos: StateFlow<List<PedidoConDetalle>> = clienteId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repo.observarPedidosCliente(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Indica de qué cliente son los pedidos a mostrar
    fun cargar(id: Int) {
        clienteId.value = id
    }

    // Observa un pedido concreto; se actualiza cuando cambia su estado
    fun observarPedido(id: Int): Flow<PedidoConDetalle?> = repo.observarPedido(id)
}
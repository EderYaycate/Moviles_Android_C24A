package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    // Catálogo completo, incluidos los productos agotados
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Crea el producto si id es 0; si no, lo actualiza
    fun guardar(producto: Producto) {
        viewModelScope.launch {
            repo.guardarProducto(producto)
        }
    }

    fun eliminar(producto: Producto) {
        viewModelScope.launch {
            repo.eliminarProducto(producto)
        }
    }

    fun cambiarDisponibilidad(id: Int, disponible: Boolean) {
        viewModelScope.launch {
            repo.cambiarDisponibilidad(id, disponible)
        }
    }
}
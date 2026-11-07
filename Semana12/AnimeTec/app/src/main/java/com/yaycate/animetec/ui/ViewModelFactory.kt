package com.yaycate.animetec.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaycate.animetec.AppContainer
import com.yaycate.animetec.ui.detalle.DetalleViewModel
import com.yaycate.animetec.ui.lista.ListaViewModel

class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ListaViewModel::class.java) ->
            ListaViewModel(container.itemRepository, container.notificador) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}

// El detalle necesita el id del anime, por eso tiene su propia factory
class DetalleViewModelFactory(
    private val container: AppContainer,
    private val id: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        DetalleViewModel(container.itemRepository, id) as T
}
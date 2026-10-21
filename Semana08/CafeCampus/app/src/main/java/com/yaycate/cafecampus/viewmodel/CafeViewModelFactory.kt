package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.yaycate.cafecampus.data.prefs.PreferenciasStore
import com.yaycate.cafecampus.data.repository.CafeRepository

class CafeViewModelFactory(
    private val repo: CafeRepository,
    private val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) ->
                SesionViewModel(repo, prefs) as T
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(repo, prefs) as T
            modelClass.isAssignableFrom(PedidosClienteViewModel::class.java) ->
                PedidosClienteViewModel(repo) as T
            modelClass.isAssignableFrom(PanelViewModel::class.java) ->
                PanelViewModel(repo) as T
            modelClass.isAssignableFrom(PedidosAdminViewModel::class.java) ->
                PedidosAdminViewModel(repo) as T
            modelClass.isAssignableFrom(MenuAdminViewModel::class.java) ->
                MenuAdminViewModel(repo) as T
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
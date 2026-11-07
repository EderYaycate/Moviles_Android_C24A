package com.yaycate.animetec.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.repository.ItemRepository
import com.yaycate.animetec.notification.Notificador
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class AdminResumen(val guardados: Int, val favoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<AdminResumen>>(UiState.Cargando)
    val estado: StateFlow<UiState<AdminResumen>> = _estado.asStateFlow()

    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    fun cargarTotales() {
        viewModelScope.launch { _estado.value = UiState.Exito(leerTotales()) }
    }

    fun sincronizar() {
        if (_ocupado.value) return
        viewModelScope.launch {
            _ocupado.value = true
            repo.refrescar()
                .onSuccess { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    _aviso.value = "Se guardaron $n elementos"
                }
                .onFailure { _aviso.value = mensajeDe(it) }
            _estado.value = UiState.Exito(leerTotales())
            _ocupado.value = false
        }
    }

    fun limpiarCache() {
        if (_ocupado.value) return
        viewModelScope.launch {
            _ocupado.value = true
            repo.limpiarCache()
            _aviso.value = "Caché limpiada"
            _estado.value = UiState.Exito(leerTotales())
            _ocupado.value = false
        }
    }

    fun avisoMostrado() { _aviso.value = null }

    private suspend fun leerTotales() =
        AdminResumen(repo.cantidadGuardada(), repo.cantidadFavoritos())

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }
}
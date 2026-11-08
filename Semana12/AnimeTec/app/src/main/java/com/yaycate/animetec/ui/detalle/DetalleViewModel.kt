package com.yaycate.animetec.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.repository.ItemRepository
import com.yaycate.animetec.domain.model.Item
import com.yaycate.animetec.translation.Traductor
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface EstadoTraduccion {
    data object Original : EstadoTraduccion
    data object Traduciendo : EstadoTraduccion
    data class Traducida(val texto: String) : EstadoTraduccion
    data class Fallo(val mensaje: String) : EstadoTraduccion
}

class DetalleViewModel(
    private val repo: ItemRepository,
    private val traductor: Traductor,
    private val id: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    private val _traduccion = MutableStateFlow<EstadoTraduccion>(EstadoTraduccion.Original)
    val traduccion: StateFlow<EstadoTraduccion> = _traduccion.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            _traduccion.value = EstadoTraduccion.Original
            repo.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun traducirSinopsis() {
        val exito = _estado.value as? UiState.Exito ?: return
        val sinopsis = exito.datos.sinopsis
        if (sinopsis.isNullOrBlank()) return
        if (_traduccion.value is EstadoTraduccion.Traduciendo) return

        _traduccion.value = EstadoTraduccion.Traduciendo
        viewModelScope.launch {
            traductor.traducirAEspanol(sinopsis)
                .onSuccess { _traduccion.value = EstadoTraduccion.Traducida(it) }
                .onFailure {
                    _traduccion.value = EstadoTraduccion.Fallo("No se pudo traducir. Revisa tu conexión e inténtalo de nuevo")
                }
        }
    }

    fun verOriginal() {
        _traduccion.value = EstadoTraduccion.Original
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }
}

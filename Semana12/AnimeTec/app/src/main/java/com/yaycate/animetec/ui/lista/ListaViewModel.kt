package com.yaycate.animetec.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.repository.ItemRepository
import com.yaycate.animetec.domain.model.Item
import com.yaycate.animetec.notification.Notificador
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            // La lista siempre sale de Room
            repo.items.collect { lista ->
                if (lista.isNotEmpty() || refrescoTerminado) _estado.value = UiState.Exito(lista)
            }
        }
        refrescar()
    }

    fun refrescar() {
        viewModelScope.launch {
            // Si veníamos de un error, volvemos a mostrar "Cargando"
            if (_estado.value is UiState.Error) _estado.value = UiState.Cargando

            val resultado = repo.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { n ->
                    if (_estado.value is UiState.Cargando) _estado.value = UiState.Exito(emptyList())
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) {
                        // Nada guardado: pantalla de error con Reintentar
                        _estado.value = UiState.Error(mensajeDe(e))
                    } else {
                        // Hay datos guardados: se muestran con un aviso
                        _aviso.value = avisoDe(e)
                    }
                }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }

    private fun avisoDe(e: Throwable) = when (e) {
        is IOException -> "Sin conexión: mostrando datos guardados"
        is HttpException -> "No se pudo actualizar (error ${e.code()}): mostrando datos guardados"
        else -> "No se pudo actualizar: mostrando datos guardados"
    }
}
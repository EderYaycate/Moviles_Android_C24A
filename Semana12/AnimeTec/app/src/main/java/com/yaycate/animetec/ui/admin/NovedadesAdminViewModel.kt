package com.yaycate.animetec.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.novedades.NovedadesRepository
import com.yaycate.animetec.domain.model.Novedad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NovedadesAdminViewModel(
    private val novedadesRepository: NovedadesRepository
) : ViewModel() {

    private val _novedades = MutableStateFlow<List<Novedad>>(emptyList())
    val novedades: StateFlow<List<Novedad>> = _novedades.asStateFlow()

    private val _publicando = MutableStateFlow(false)
    val publicando: StateFlow<Boolean> = _publicando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        observar()
    }

    private fun observar() {
        viewModelScope.launch {
            novedadesRepository.observar()
                .catch { e ->
                    _mensaje.value = e.message ?: "Error al observar novedades"
                }
                .collect { lista ->
                    _novedades.value = lista
                }
        }
    }

    fun publicar(titulo: String, mensaje: String, animeIdTexto: String) {
        val tTrim = titulo.trim()
        val mTrim = mensaje.trim()

        if (tTrim.isEmpty() || tTrim.length > 60) {
            _mensaje.value = "El título no puede estar vacío ni superar los 60 caracteres"
            return
        }
        if (mTrim.isEmpty() || mTrim.length > 300) {
            _mensaje.value = "El mensaje no puede estar vacío ni superar los 300 caracteres"
            return
        }

        val animeId = if (animeIdTexto.isNotBlank()) {
            if (!animeIdTexto.all { it.isDigit() }) {
                _mensaje.value = "El ID del anime debe contener solo dígitos"
                return
            }
            animeIdTexto.toIntOrNull()
        } else {
            null
        }

        viewModelScope.launch {
            _publicando.value = true
            novedadesRepository.publicar(tTrim, mTrim, animeId)
                .onSuccess {
                    _mensaje.value = "Novedad publicada"
                }
                .onFailure { e ->
                    _mensaje.value = e.message ?: "Error al publicar novedad"
                }
            _publicando.value = false
        }
    }

    fun eliminar(id: String) {
        viewModelScope.launch {
            novedadesRepository.eliminar(id)
                .onSuccess {
                    _mensaje.value = "Novedad eliminada"
                }
                .onFailure { e ->
                    _mensaje.value = e.message ?: "Error al eliminar novedad"
                }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}

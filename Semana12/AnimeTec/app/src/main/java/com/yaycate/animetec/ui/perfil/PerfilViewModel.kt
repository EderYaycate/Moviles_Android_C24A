package com.yaycate.animetec.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.auth.AuthRepository
import com.yaycate.animetec.data.profile.PerfilRepository
import com.yaycate.animetec.data.profile.ProcesadorFoto
import com.yaycate.animetec.domain.model.Perfil
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val authRepository: AuthRepository,
    private val perfilRepository: PerfilRepository,
    private val procesadorFoto: ProcesadorFoto
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Perfil>>(UiState.Cargando)
    val estado: StateFlow<UiState<Perfil>> = _estado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            val sesion = authRepository.sesionActual()
            if (sesion == null) {
                _estado.value = UiState.Error("No hay sesión activa")
                return@launch
            }
            perfilRepository.obtener(sesion.uid)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "Error al cargar perfil") }
        }
    }

    fun guardarNombre(nombre: String) {
        val trimmed = nombre.trim()
        if (trimmed.isEmpty()) {
            _mensaje.value = "El nombre no puede estar vacío"
            return
        }
        if (trimmed.length > 30) {
            _mensaje.value = "El nombre no puede tener más de 30 caracteres"
            return
        }

        val exitoActual = _estado.value as? UiState.Exito ?: return

        viewModelScope.launch {
            _guardando.value = true
            val sesion = authRepository.sesionActual()
            if (sesion == null) {
                _guardando.value = false
                return@launch
            }
            perfilRepository.guardarNombre(sesion.uid, trimmed)
                .onSuccess {
                    _estado.value = UiState.Exito(exitoActual.datos.copy(nombre = trimmed))
                    _mensaje.value = "Perfil actualizado"
                }
                .onFailure {
                    _mensaje.value = it.message ?: "Error al guardar el nombre"
                }
            _guardando.value = false
        }
    }

    fun cambiarFoto(uri: String) {
        val exitoActual = _estado.value as? UiState.Exito ?: return

        viewModelScope.launch {
            _guardando.value = true
            val sesion = authRepository.sesionActual()
            if (sesion == null) {
                _guardando.value = false
                return@launch
            }
            procesadorFoto.aBase64(uri)
                .onSuccess { base64 ->
                    perfilRepository.guardarFoto(sesion.uid, base64)
                        .onSuccess {
                            _estado.value = UiState.Exito(exitoActual.datos.copy(fotoBase64 = base64))
                            _mensaje.value = "Perfil actualizado"
                        }
                        .onFailure {
                            _mensaje.value = it.message ?: "Error al guardar la foto"
                        }
                }
                .onFailure {
                    _mensaje.value = it.message ?: "Error al procesar la imagen"
                }
            _guardando.value = false
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}

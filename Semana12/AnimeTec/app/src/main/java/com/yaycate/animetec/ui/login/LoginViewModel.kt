package com.yaycate.animetec.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.auth.AuthRepository
import com.yaycate.animetec.data.auth.CredencialesInvalidasException
import com.yaycate.animetec.data.auth.SinConexionException
import com.yaycate.animetec.domain.model.Sesion
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    init {
        // Sesión persistente: si ya hay un usuario, entra directo
        viewModelScope.launch { _estado.value = UiState.Exito(repo.sesionActual()) }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error("Ingresa tu correo y tu contraseña")
            return // no se llama a Firebase
        }
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repo.iniciarSesion(correo.trim(), clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarError() {
        if (_estado.value is UiState.Error) _estado.value = UiState.Exito(null)
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is CredencialesInvalidasException -> "Correo o contraseña incorrectos"
        is SinConexionException -> "No hay conexión a internet"
        else -> "No se pudo iniciar sesión. Intenta de nuevo"
    }
}

class SesionViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    init {
        // Si Android recrea el proceso, recupera la sesión y su rol
        viewModelScope.launch { _sesion.value = repo.sesionActual() }
    }

    fun establecer(sesion: Sesion) { _sesion.value = sesion }

    fun cerrarSesion() {
        repo.cerrarSesion()
        _sesion.value = null
    }
}
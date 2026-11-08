package com.yaycate.animetec.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.auth.AuthRepository
import com.yaycate.animetec.data.auth.ClaveDebilException
import com.yaycate.animetec.data.auth.CorreoInvalidoException
import com.yaycate.animetec.data.auth.CorreoYaRegistradoException
import com.yaycate.animetec.data.auth.SinConexionException
import com.yaycate.animetec.domain.model.Sesion
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Exito(null))
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    fun registrar(correo: String, clave: String, confirmarClave: String) {
        val correoTrim = correo.trim()
        if (correoTrim.isBlank() || !correoTrim.contains("@")) {
            _estado.value = UiState.Error("Ingresa un correo electrónico válido")
            return
        }
        if (clave.length < 6) {
            _estado.value = UiState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        if (clave != confirmarClave) {
            _estado.value = UiState.Error("Las contraseñas no coinciden")
            return
        }

        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repo.registrar(correoTrim, clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarError() {
        if (_estado.value is UiState.Error) _estado.value = UiState.Exito(null)
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is CorreoYaRegistradoException -> "Este correo ya está registrado"
        is ClaveDebilException -> "La contraseña es muy débil"
        is CorreoInvalidoException -> "Correo electrónico inválido"
        is SinConexionException -> "No hay conexión a internet"
        else -> "No se pudo crear la cuenta. Intenta de nuevo"
    }
}

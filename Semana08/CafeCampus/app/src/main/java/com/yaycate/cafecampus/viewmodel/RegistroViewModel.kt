package com.yaycate.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: CafeRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _exito = MutableStateFlow(false)
    val exito: StateFlow<Boolean> = _exito.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun registrar(
        nombre: String,
        correo: String,
        clave: String,
        confirmarClave: String
    ) {
        _error.value = null

        if (nombre.isBlank() || correo.isBlank() || clave.isBlank() || confirmarClave.isBlank()) {
            _error.value = "Todos los campos son obligatorios"
            return
        }

        if (clave != confirmarClave) {
            _error.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            val resultado = repo.registrarCliente(
                nombre = nombre,
                correo = correo,
                clave = clave
            )
            _cargando.value = false

            resultado.onSuccess {
                _exito.value = true
            }.onFailure { ex ->
                _error.value = ex.message ?: "Ocurrió un error al registrar"
            }
        }
    }

    fun limpiarEstado() {
        _error.value = null
        _exito.value = false
    }
}
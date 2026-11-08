package com.yaycate.animetec.ui.novedades

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.animetec.data.auth.AuthRepository
import com.yaycate.animetec.data.novedades.LecturaNovedades
import com.yaycate.animetec.data.novedades.NovedadesRepository
import com.yaycate.animetec.domain.model.Novedad
import com.yaycate.animetec.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class NovedadesUiState(
    val novedades: List<Novedad> = emptyList(),
    val sinLeer: Int = 0,
    val ultimaVistaAnterior: Long = 0L,
    val error: String? = null
)

class NovedadesViewModel(
    private val authRepository: AuthRepository,
    private val novedadesRepository: NovedadesRepository,
    private val lecturaNovedades: LecturaNovedades,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow(NovedadesUiState())
    val uiState: StateFlow<NovedadesUiState> = _uiState.asStateFlow()

    private var isFirstEmission = true
    private var idsConocidos = setOf<String>()

    init {
        observarNovedades()
    }

    private fun observarNovedades() {
        viewModelScope.launch {
            val sesion = authRepository.sesionActual()
            if (sesion == null) {
                _uiState.value = _uiState.value.copy(error = "No hay sesión activa")
                return@launch
            }
            val ultimaVista = lecturaNovedades.ultimaVista(sesion.uid)

            novedadesRepository.observar()
                .catch { e ->
                    Log.e("NOVEDADES", "Error al observar novedades", e)
                    _uiState.value = _uiState.value.copy(error = e.message ?: "Error al observar novedades")
                }
                .collect { lista ->
                    val sinLeerCount = lista.count { it.fecha > ultimaVista }

                    if (isFirstEmission) {
                        isFirstEmission = false
                        idsConocidos = lista.map { it.id }.toSet()
                    } else {
                        for (nov in lista) {
                            if (nov.id !in idsConocidos && nov.fecha > ultimaVista) {
                                notificador.mostrar(nov.titulo, nov.mensaje)
                            }
                        }
                        idsConocidos = lista.map { it.id }.toSet()
                    }

                    _uiState.value = _uiState.value.copy(
                        novedades = lista,
                        sinLeer = sinLeerCount,
                        ultimaVistaAnterior = ultimaVista,
                        error = null
                    )
                }
        }
    }

    fun marcarVistas() {
        viewModelScope.launch {
            val sesion = authRepository.sesionActual() ?: return@launch
            val lista = _uiState.value.novedades
            if (lista.isEmpty()) return@launch

            val fechaMasReciente = lista.maxOf { it.fecha }
            val ultimaVistaActual = lecturaNovedades.ultimaVista(sesion.uid)

            lecturaNovedades.marcarVistas(sesion.uid, fechaMasReciente)

            _uiState.value = _uiState.value.copy(
                sinLeer = 0,
                ultimaVistaAnterior = ultimaVistaActual
            )
        }
    }
}

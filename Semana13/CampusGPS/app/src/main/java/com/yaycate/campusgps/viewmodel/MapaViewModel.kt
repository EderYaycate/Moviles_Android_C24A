package com.yaycate.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.campusgps.data.LugaresRepository
import com.yaycate.campusgps.data.UbicacionRepository
import com.yaycate.campusgps.model.LugarConDistancia
import com.yaycate.campusgps.model.Posicion
import com.yaycate.campusgps.model.UbicacionUi
import com.yaycate.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    val masCercano: StateFlow<LugarConDistancia?> = _lugares
        .map { lista -> lista.firstOrNull { it.metros != null } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val masCercanoId: StateFlow<Int?> = masCercano
        .map { it?.lugar?.id }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun buscar() {
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = UbicacionUi.Error(mensajeDe(it)) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás")
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    fun irAlMasCercano() {
        _seleccionado.value = masCercano.value?.lugar?.id
    }

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = _lugares.value
            .map { item ->
                val destino = Posicion(item.lugar.latitud, item.lugar.longitud)
                item.copy(metros = distanciaMetros(posicion, destino))
            }
            .sortedBy { it.metros }
    }

    private fun mensajeDe(e: Throwable): String = when (e) {
        is SecurityException -> "Falta el permiso de ubicación"
        else -> e.message ?: "No se pudo obtener la ubicación"
    }
}
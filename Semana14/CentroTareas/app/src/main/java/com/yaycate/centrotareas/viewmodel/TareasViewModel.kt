package com.yaycate.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.yaycate.centrotareas.data.TareasRepository
import com.yaycate.centrotareas.model.CargaUi
import com.yaycate.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4) -----
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    fun tareaLargaMala() {
        _mensaje.value = "Trabajando..."
        Thread.sleep(4000)
        _mensaje.value = "Terminó"
    }

    // ----- Contar primos (paso 5) -----
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    // ----- Dos cargas (pasos 6 y 7) -----
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()
}

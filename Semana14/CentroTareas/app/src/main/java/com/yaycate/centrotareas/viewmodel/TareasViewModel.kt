package com.yaycate.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaycate.centrotareas.data.TareasRepository
import com.yaycate.centrotareas.model.CargaUi
import com.yaycate.centrotareas.model.ComparacionTiempos
import com.yaycate.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val LIMITE_POR_DEFECTO_MS = 20_000L
private const val REINTENTOS = 3

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tiempo máximo (mejora 1) -----
    private val _limiteMs = MutableStateFlow(LIMITE_POR_DEFECTO_MS)
    val limiteMs: StateFlow<Long> = _limiteMs.asStateFlow()

    fun cambiarLimite(ms: Long) {
        _limiteMs.value = ms
    }

    // ----- Historial de resultados (mejora 3): ultimos 5, en memoria -----
    private val _historial = MutableStateFlow<List<String>>(emptyList())
    val historial: StateFlow<List<String>> = _historial.asStateFlow()

    private fun registrar(texto: String) {
        _historial.value = (listOf(texto) + _historial.value).take(5)
    }

    // ----- Tarea con progreso (pasos 1 a 4) -----
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private var trabajo: Job? = null

    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                withTimeout(_limiteMs.value) {
                    withContext(Dispatchers.Default) {
                        Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
                        for (i in 1..100) {
                            delay(40)
                            _progreso.value = i / 100f
                        }
                    }
                }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } catch (e: TimeoutCancellationException) {
                _mensaje.value = "Tardó demasiado"
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ----- Contar primos (paso 5) -----
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (_primos.value == PrimosUi.Calculando) return
        _primos.value = PrimosUi.Calculando
        trabajoPrimos = viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                val ms = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, ms)
                registrar("Primos hasta $hasta: $cantidad en $ms ms")
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        if (trabajoPrimos?.isActive != true) return
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7) -----
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    // ----- Comparacion de tiempos (mejora 4) -----
    private val _comparacion = MutableStateFlow(ComparacionTiempos())
    val comparacion: StateFlow<ComparacionTiempos> = _comparacion.asStateFlow()

    private fun exito(modo: String, perfil: String, pedidos: Int, milisegundos: Long) {
        _carga.value = CargaUi.Exito(modo, perfil, pedidos, milisegundos)
        val actual = _comparacion.value
        _comparacion.value = if (modo == "Una por una") {
            actual.copy(unaPorUna = milisegundos)
        } else {
            actual.copy(aLaVez = milisegundos)
        }
        registrar("$modo: $milisegundos ms, $pedidos pedidos")
    }

    // Reintento automatico (mejora 2): hasta REINTENTOS veces con espera creciente.
    // Si todos fallan, el ultimo intento deja pasar el error.
    private suspend fun <T> conReintentos(bloque: suspend () -> T): T {
        var espera = 200L
        repeat(REINTENTOS) {
            try {
                return bloque()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                delay(espera)
                espera *= 2
            }
        }
        return bloque()
    }

    fun cargarUnaPorUna() {
        if (_carga.value == CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                withTimeout(_limiteMs.value) {
                    val perfil = repo.cargarPerfil()
                    val pedidos = conReintentos { repo.cargarPedidos(_simularError.value) }
                    exito(
                        modo = "Una por una",
                        perfil = perfil,
                        pedidos = pedidos.size,
                        milisegundos = System.currentTimeMillis() - inicio
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _carga.value = CargaUi.Error("Tardó demasiado")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value == CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                withTimeout(_limiteMs.value) {
                    val (perfil, pedidos) = coroutineScope {
                        val a = async { repo.cargarPerfil() }
                        val b = async { conReintentos { repo.cargarPedidos(_simularError.value) } }
                        Pair(a.await(), b.await())
                    }
                    exito(
                        modo = "A la vez",
                        perfil = perfil,
                        pedidos = pedidos.size,
                        milisegundos = System.currentTimeMillis() - inicio
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _carga.value = CargaUi.Error("Tardó demasiado")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }
}

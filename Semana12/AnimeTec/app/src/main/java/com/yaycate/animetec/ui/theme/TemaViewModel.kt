package com.yaycate.animetec.ui.theme

import androidx.lifecycle.ViewModel
import com.yaycate.animetec.data.settings.AjustesTema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemaViewModel(
    private val ajustes: AjustesTema
) : ViewModel() {

    private val _modo = MutableStateFlow(ajustes.leer())
    val modo: StateFlow<ModoTema> = _modo.asStateFlow()

    fun cambiar(modo: ModoTema) {
        _modo.value = modo
        ajustes.guardar(modo)
    }
}

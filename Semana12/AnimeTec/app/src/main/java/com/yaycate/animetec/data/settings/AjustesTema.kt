package com.yaycate.animetec.data.settings

import com.yaycate.animetec.ui.theme.ModoTema

interface AjustesTema {
    fun leer(): ModoTema
    fun guardar(modo: ModoTema)
}

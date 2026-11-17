package com.yaycate.campusgps.data

import kotlinx.coroutines.flow.Flow

interface TemaRepository {
    val modoOscuro: Flow<Boolean>
    suspend fun guardarModoOscuro(oscuro: Boolean)
}
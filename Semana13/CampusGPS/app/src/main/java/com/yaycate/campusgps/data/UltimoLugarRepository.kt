package com.yaycate.campusgps.data

import kotlinx.coroutines.flow.Flow

interface UltimoLugarRepository {
    val ultimoLugar: Flow<Int?>
    suspend fun guardarUltimo(id: Int)
}

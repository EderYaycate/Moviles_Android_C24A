package com.yaycate.campusgps.data

import com.yaycate.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}
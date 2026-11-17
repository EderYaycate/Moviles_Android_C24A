package com.yaycate.campusgps.data

import com.yaycate.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
package com.yaycate.campusgps.data

import com.yaycate.campusgps.model.Lugar

class LugaresRepositoryLocal : LugaresRepository {
    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(1, "Biblioteca Central", "Estudio", -12.0443, -76.9534),
        Lugar(2, "Cafetería Campus", "Comida", -12.0448, -76.9535),
        Lugar(3, "Laboratorio de Cómputo", "Estudio", -12.0443, -76.9530),
        Lugar(4, "Auditorio Principal", "Eventos", -12.0445, -76.9533),
        Lugar(5, "Estacionamiento Norte", "Servicios", -12.0441, -76.9532)
    )
}
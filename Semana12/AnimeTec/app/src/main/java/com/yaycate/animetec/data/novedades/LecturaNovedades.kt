package com.yaycate.animetec.data.novedades

interface LecturaNovedades {
    fun ultimaVista(uid: String): Long
    fun marcarVistas(uid: String, hasta: Long)
}

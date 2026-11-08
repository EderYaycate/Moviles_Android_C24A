package com.yaycate.animetec.domain.model

data class Novedad(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val animeId: Int?,
    val fecha: Long
)

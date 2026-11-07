package com.yaycate.animetec.domain.model

data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val puntaje: String? = null,
    val esFavorito: Boolean = false,
    // Solo se llenan en el detalle (segundo endpoint)
    val sinopsis: String? = null,
    val episodios: Int? = null,
    val estado: String? = null,
    val fechaInicio: String? = null
)
package com.yaycate.animetec.data.mapper

import com.yaycate.animetec.data.remote.dto.AnimeDataDto
import com.yaycate.animetec.domain.model.Item

fun AnimeDataDto.aModelo(): Item {
    val a = attributes
    val tipo = a.subtype?.uppercase() ?: "N/D"
    val episodios = a.episodeCount?.let { "$it episodios" } ?: "episodios N/D"
    return Item(
        id = id.toInt(),                       // Kitsu manda el id como texto
        titulo = a.canonicalTitle ?: "Sin título",
        subtitulo = "$tipo · $episodios",
        imagenUrl = a.posterImage?.medium,
        puntaje = a.averageRating,
        sinopsis = a.synopsis,
        episodios = a.episodeCount,
        estado = a.status,
        fechaInicio = a.startDate
    )
}
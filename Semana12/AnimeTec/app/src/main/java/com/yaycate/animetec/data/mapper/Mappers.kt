package com.yaycate.animetec.data.mapper

import com.yaycate.animetec.data.local.ItemEntity
import com.yaycate.animetec.data.remote.dto.AnimeDataDto
import com.yaycate.animetec.domain.model.Item

private fun AnimeDataDto.subtitulo(): String {
    val tipo = attributes.subtype?.uppercase() ?: "N/D"
    val episodios = attributes.episodeCount?.let { "$it episodios" } ?: "episodios N/D"
    return "$tipo · $episodios"
}

// DTO (API) -> Entity (Room)
fun AnimeDataDto.aEntity(esFavorito: Boolean = false) = ItemEntity(
    id = id.toInt(),                       // Kitsu manda el id como texto
    titulo = attributes.canonicalTitle ?: "Sin título",
    subtitulo = subtitulo(),
    imagenUrl = attributes.posterImage?.medium,
    puntaje = attributes.averageRating,
    esFavorito = esFavorito
)

// Entity (Room) -> modelo de la UI
fun ItemEntity.aModelo() = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    puntaje = puntaje,
    esFavorito = esFavorito
)

// DTO (API) -> modelo de la UI, solo para el detalle (trae sinopsis, estado, etc.)
fun AnimeDataDto.aModelo() = Item(
    id = id.toInt(),
    titulo = attributes.canonicalTitle ?: "Sin título",
    subtitulo = subtitulo(),
    imagenUrl = attributes.posterImage?.large ?: attributes.posterImage?.medium,
    puntaje = attributes.averageRating,
    sinopsis = attributes.synopsis,
    episodios = attributes.episodeCount,
    estado = attributes.status,
    fechaInicio = attributes.startDate
)
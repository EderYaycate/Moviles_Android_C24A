package com.yaycate.animetec.data.remote.dto

import kotlinx.serialization.Serializable

// Lista: "data" es un arreglo
@Serializable
data class ListaResponseDto(val data: List<AnimeDataDto>)

// Detalle: "data" es un solo objeto
@Serializable
data class DetalleResponseDto(val data: AnimeDataDto)

@Serializable
data class AnimeDataDto(
    val id: String,                    // Kitsu manda el id como texto
    val attributes: AnimeAttributesDto
)

// Casi todo es nullable: Kitsu devuelve null en varios campos
@Serializable
data class AnimeAttributesDto(
    val canonicalTitle: String? = null,
    val synopsis: String? = null,
    val averageRating: String? = null, // viene como texto: "88.86"
    val episodeCount: Int? = null,
    val subtype: String? = null,
    val status: String? = null,
    val startDate: String? = null,
    val posterImage: PosterImageDto? = null
)

@Serializable
data class PosterImageDto(
    val medium: String? = null,
    val large: String? = null
)
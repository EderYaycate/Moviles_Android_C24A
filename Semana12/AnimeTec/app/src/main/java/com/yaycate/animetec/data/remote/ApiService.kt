package com.yaycate.animetec.data.remote

import com.yaycate.animetec.data.remote.dto.DetalleResponseDto
import com.yaycate.animetec.data.remote.dto.ListaResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Endpoint 1: lista (top por puntaje)
    @GET("anime")
    suspend fun listar(
        @Query("sort") orden: String = "-averageRating",
        @Query("page[limit]") limite: Int = 20
    ): ListaResponseDto

    // Endpoint 2: detalle
    @GET("anime/{id}")
    suspend fun detalle(@Path("id") id: Int): DetalleResponseDto
}
package com.yaycate.animetec.data.novedades

import com.yaycate.animetec.domain.model.Novedad
import kotlinx.coroutines.flow.Flow

interface NovedadesRepository {
    fun observar(): Flow<List<Novedad>>
}

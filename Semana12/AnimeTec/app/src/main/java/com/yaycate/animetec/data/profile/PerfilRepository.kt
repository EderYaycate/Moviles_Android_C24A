package com.yaycate.animetec.data.profile

import com.yaycate.animetec.domain.model.Perfil

interface PerfilRepository {
    suspend fun obtener(uid: String): Result<Perfil>
    suspend fun guardarNombre(uid: String, nombre: String): Result<Unit>
    suspend fun guardarFoto(uid: String, fotoBase64: String): Result<Unit>
}

package com.yaycate.animetec.data.auth

import com.yaycate.animetec.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun sesionActual(): Sesion? // null si no hay sesión
    fun cerrarSesion()
    suspend fun recuperarClave(correo: String): Result<Unit>
}

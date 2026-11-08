package com.yaycate.animetec.domain.model

data class Perfil(
    val nombre: String,
    val correo: String,
    val rol: Rol,
    val fotoBase64: String?
)

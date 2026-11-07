package com.yaycate.animetec.domain.model

enum class Rol { ADMIN, USUARIO }

data class Sesion(val uid: String, val correo: String, val rol: Rol)
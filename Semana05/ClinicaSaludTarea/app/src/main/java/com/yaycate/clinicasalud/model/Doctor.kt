package com.yaycate.clinicasalud.model

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Float
)
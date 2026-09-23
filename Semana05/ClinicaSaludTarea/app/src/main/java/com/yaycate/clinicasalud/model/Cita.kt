package com.yaycate.clinicasalud.model

data class Cita(
    val doctor: Doctor,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada" // "Confirmada" o "Completada"
)
package com.yaycate.tecsupfit

data class ClaseGym(
    val id: Int,
    val nombre: String,
    val instructor: String,
    val dia: String,           // "Hoy" o "Esta semana" -> para el filtro
    val cuposDisponibles: List<String> // mínimo 3 horarios/cupos
)

data class Reserva(
    val id: Int,
    val clase: ClaseGym,
    val horarioElegido: String,
    var estado: String // "Confirmada" o "Completada"
)

data class UsuarioPerfil(
    val nombre: String,
    val clasesTomadas: Int,
    val rachaDias: Int
)

fun clasesDeEjemplo(): List<ClaseGym> = listOf(
    ClaseGym(1, "Spinning", "Prof. Diego Ruiz", "Hoy", listOf("7:00 AM", "12:00 PM", "6:00 PM")),
    ClaseGym(2, "Yoga Flow", "Prof. Carla Nuñez", "Hoy", listOf("8:00 AM", "5:00 PM", "7:30 PM")),
    ClaseGym(3, "Funcional HIIT", "Prof. Renzo Vidal", "Esta semana", listOf("6:00 AM", "1:00 PM", "8:00 PM")),
    ClaseGym(4, "Zumba", "Prof. Mía Flores", "Esta semana", listOf("9:00 AM", "4:00 PM", "7:00 PM")),
    ClaseGym(5, "Box Fit", "Prof. Iván Soto", "Esta semana", listOf("6:30 AM", "2:00 PM", "9:00 PM"))
)
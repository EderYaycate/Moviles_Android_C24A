package com.yaycate.tecsupfit

data class ClaseGym(
    val id: Int,
    val nombre: String,
    val horarioGeneral: String,
    val sala: String,
    val duracionMinutos: Int,
    val descripcion: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val filtro: String
)

data class Reserva(
    val id: Int,
    val clase: ClaseGym,
    val horarioElegido: String,
    val estado: String
)

data class UsuarioPerfil(
    val nombre: String,
    val plan: String = "Plan Premium",
    val clasesTomadas: Int,
    val rachaDias: Int
) {
    val iniciales: String
        get() = nombre.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
            .uppercase()
}

// Datos de ejemplo para poblar la vista
fun clasesDeEjemplo(): List<ClaseGym> {
    return listOf(
        ClaseGym(
            id = 1,
            nombre = "Yoga funcional",
            horarioGeneral = "7:00 am",
            sala = "Sala 2",
            duracionMinutos = 45,
            descripcion = "Entrenamiento para flexibilidad y fuerza postural.",
            cuposDisponibles = 5,
            cuposTotales = 15,
            filtro = "Hoy"
        ),
        ClaseGym(
            id = 2,
            nombre = "Cross Training",
            horarioGeneral = "6:00 pm",
            sala = "Sala 1",
            duracionMinutos = 50,
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            cuposDisponibles = 8,
            cuposTotales = 12,
            filtro = "Hoy"
        ),
        ClaseGym(
            id = 3,
            nombre = "Spinning",
            horarioGeneral = "7:30 pm",
            sala = "Sala 3",
            duracionMinutos = 45,
            descripcion = "Cardio de alta intensidad sobre bicicleta estática.",
            cuposDisponibles = 3,
            cuposTotales = 20,
            filtro = "Hoy"
        )
    )
}
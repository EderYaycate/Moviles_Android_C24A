package com.yaycate.clinicasalud.data

import com.yaycate.clinicasalud.model.Doctor

object DataSource {

    val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología", "Odontología")

    val medicos = listOf(
        Doctor(1, "Dr. Carlos Ramírez", "Cardiología", 4.8f),
        Doctor(2, "Dra. Lucía Fernández", "Pediatría", 4.9f),
        Doctor(3, "Dr. Miguel Torres", "Dermatología", 4.6f),
        Doctor(4, "Dra. Ana Quispe", "Odontología", 4.7f),
        Doctor(5, "Dr. Jorge Salas", "Cardiología", 4.5f)
    )

    val horasDisponibles = listOf("09:00 am", "11:00 am", "03:00 pm")
    val fechasDisponibles = listOf("Lun 22 Sep", "Mié 24 Sep", "Vie 26 Sep")
}
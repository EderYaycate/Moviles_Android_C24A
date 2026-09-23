package com.yaycate.clinicasalud.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen(route = "inicio")

    object Perfil : Screen(route = "perfil/{doctorId}") {
        fun createRoute(doctorId: Int) = "perfil/$doctorId"
    }

    object Confirmacion : Screen(route = "confirmacion/{doctorId}/{fecha}/{hora}") {
        fun createRoute(doctorId: Int, fecha: String, hora: String) =
            "confirmacion/$doctorId/$fecha/$hora"
    }

    object MisCitas : Screen(route = "mis_citas")
    object Historial : Screen(route = "historial")
}
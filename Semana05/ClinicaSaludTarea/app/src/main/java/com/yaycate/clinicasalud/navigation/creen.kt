package com.yaycate.clinicasalud.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Perfil : Screen("perfil/{doctorId}") {
        fun createRoute(doctorId: Int) = "perfil/$doctorId"
    }
    object Confirmacion : Screen("confirmacion")
    object MisCitas : Screen("mis_citas")
    object Historial : Screen("historial")
}
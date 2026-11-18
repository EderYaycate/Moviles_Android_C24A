package com.yaycate.centrotareas.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Colores copiados de CampusGPS (ui/Colores.kt); no se importan de otro módulo.
val AzulCampus = Color(0xFF0B2545)
val AzulMarinoClaro = Color(0xFF1F5FA8)
val AzulSuave = Color(0xFFE3EEFB)
val AzulAcentoOscuro = Color(0xFF8AB4F8)

val DegradadoAzul = Brush.horizontalGradient(
    colors = listOf(AzulCampus, AzulMarinoClaro)
)

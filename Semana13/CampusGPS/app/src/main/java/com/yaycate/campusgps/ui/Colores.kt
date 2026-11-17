package com.yaycate.campusgps.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

val AzulCampus = Color(0xFF0B2545)
val AzulMarinoClaro = Color(0xFF1F5FA8)
val AzulSuave = Color(0xFFE3EEFB)
val AzulAcentoOscuro = Color(0xFF8AB4F8)
val AzulSeleccionOscuro = Color(0xFF1E3A5F)

val DegradadoAzul = Brush.horizontalGradient(
    colors = listOf(AzulCampus, AzulMarinoClaro)
)

@Composable
fun esTemaOscuro(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

@Composable
fun acentoTema(): Color = if (esTemaOscuro()) AzulAcentoOscuro else AzulCampus

@Composable
fun fondoSeleccionTema(): Color = if (esTemaOscuro()) AzulSeleccionOscuro else AzulSuave
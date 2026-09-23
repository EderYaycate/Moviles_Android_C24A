package com.yaycate.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Rutina(val nombre: String, val duracionMin: Int, val nivel: String)

fun rutinasDeEjemplo(): List<Rutina> = listOf(
    Rutina("Full Body Básico", 30, "Principiante"),
    Rutina("Cardio Intenso", 25, "Intermedio"),
    Rutina("Fuerza - Tren superior", 40, "Avanzado")
)

@Composable
fun RutinasScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rutinasDeEjemplo()) { rutina ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = rutina.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${rutina.duracionMin} min · ${rutina.nivel}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
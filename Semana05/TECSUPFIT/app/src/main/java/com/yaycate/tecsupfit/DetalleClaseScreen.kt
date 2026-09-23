package com.yaycate.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetalleClaseScreen(clase: ClaseGym, onReservar: (String) -> Unit) {
    var horarioElegido by remember { mutableStateOf(clase.cuposDisponibles.first()) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(clase.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Instructor: ${clase.instructor}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(Modifier.height(24.dp))
        Text("Elige tu horario / cupo:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        // Selección única de horario -> visualmente parece lista, lógicamente es un RadioButton
        Column {
            clase.cuposDisponibles.forEach { horario ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = horarioElegido == horario,
                        onClick = { horarioElegido = horario }
                    )
                    Text(horario)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onReservar(horarioElegido) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}
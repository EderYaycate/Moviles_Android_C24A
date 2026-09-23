package com.yaycate.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.clinicasalud.data.DataSource
import com.yaycate.clinicasalud.model.Doctor

@Composable
fun PerfilMedicoScreen(
    doctor: Doctor,
    onAgendarClick: (fecha: String, hora: String) -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = doctor.nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = doctor.especialidad,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Selecciona una fecha", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DataSource.fechasDisponibles) { fecha ->
                FilterChip(
                    selected = fecha == fechaSeleccionada,
                    onClick = { fechaSeleccionada = fecha },
                    label = { Text(fecha) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Selecciona una hora", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DataSource.horasDisponibles) { hora ->
                FilterChip(
                    selected = hora == horaSeleccionada,
                    onClick = { horaSeleccionada = hora },
                    label = { Text(hora) }
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (fechaSeleccionada != null && horaSeleccionada != null) {
                    onAgendarClick(fechaSeleccionada!!, horaSeleccionada!!)
                }
            },
            enabled = fechaSeleccionada != null && horaSeleccionada != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Agendar cita")
        }
    }
}
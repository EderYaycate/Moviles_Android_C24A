package com.yaycate.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.clinicasalud.model.Doctor

@Composable
fun ConfirmacionScreen(
    doctor: Doctor,
    fecha: String,
    hora: String,
    onVolverInicioClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Confirmado",
            modifier = Modifier.size(72.dp)
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Cita agendada!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Médico: ${doctor.nombre}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Especialidad: ${doctor.especialidad}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Fecha: $fecha", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Hora: $hora", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onVolverInicioClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}
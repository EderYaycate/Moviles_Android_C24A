package com.yaycate.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.clinicasalud.model.Cita

@Composable
fun MisCitasScreen(citas: List<Cita>) {
    if (citas.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Aún no tienes citas agendadas")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(citas) { cita ->
            Card(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = cita.doctor.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = cita.doctor.especialidad, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "${cita.fecha} - ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
                    }

                    val colorEstado = if (cita.estado == "Confirmada") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    }

                    AssistChip(
                        onClick = { },
                        label = { Text(cita.estado) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = colorEstado
                        )
                    )
                }
            }
        }
    }
}
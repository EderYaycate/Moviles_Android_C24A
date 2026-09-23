package com.yaycate.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.clinicasalud.model.Cita

@Composable
fun MisCitasScreen(
    citas: List<Cita>,
    onCancelarCita: (Cita) -> Unit,
    modifier: Modifier = Modifier
) {
    var citaAEliminar by remember { mutableStateOf<Cita?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis citas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (citas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.EventBusy,
                        contentDescription = "Sin citas",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Aún no tienes citas agendadas",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas) { cita ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Barra vertical morada de acento para la cita "Confirmada" (como en el mockup)
                            if (cita.estado == "Confirmada") {
                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .fillMaxHeight()
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                                        )
                                )
                            } else {
                                Spacer(modifier = Modifier.width(6.dp))
                            }

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = cita.doctor.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${cita.fecha}, ${cita.hora}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Badge / Chip visual para el Estado
                                    val isConfirmada = cita.estado == "Confirmada"
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (isConfirmada) Color(0xFFE8F5E9) else Color(0xFFEEEEEE)
                                    ) {
                                        Text(
                                            text = cita.estado,
                                            color = if (isConfirmada) Color(0xFF2E7D32) else Color(0xFF757575),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                // Botón de basurero para cancelar cita
                                if (cita.estado == "Confirmada") {
                                    IconButton(onClick = { citaAEliminar = cita }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Cancelar cita",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de Confirmación
        citaAEliminar?.let { cita ->
            AlertDialog(
                onDismissRequest = { citaAEliminar = null },
                title = { Text(text = "Cancelar cita") },
                text = {
                    Text(
                        text = "¿Estás seguro de que deseas cancelar la cita con el Dr(a). ${cita.doctor.nombre} para el ${cita.fecha} a las ${cita.hora}?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onCancelarCita(cita)
                            citaAEliminar = null
                        }
                    ) {
                        Text("Confirmar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { citaAEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
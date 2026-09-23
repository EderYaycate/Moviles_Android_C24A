package com.yaycate.tecsupfit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

@Composable
fun ReservasScreen(
    reservas: List<Reserva>,
    onCancelarReserva: (Reserva) -> Unit = {}
) {
    // Estado local para el filtro de estados
    var filtroEstado by remember { mutableStateOf("Todas") }
    // Estado local para controlar el AlertDialog de cancelación
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }

    // Filtrado dinámico de la lista
    val reservasFiltradas = when (filtroEstado) {
        "Confirmadas" -> reservas.filter { it.estado == "Confirmada" }
        "Completadas" -> reservas.filter { it.estado == "Completada" }
        else -> reservas
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis reservas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Chips de filtro por estado
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(listOf("Todas", "Confirmadas", "Completadas")) { estado ->
                FilterChip(
                    selected = filtroEstado == estado,
                    onClick = { filtroEstado = estado },
                    label = { Text(estado) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF005A44),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (reservasFiltradas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.EventBusy,
                        contentDescription = "Sin reservas",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "No hay reservas en este estado", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(reservasFiltradas) { reserva ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (reserva.estado == "Confirmada") {
                                Box(
                                    modifier = Modifier
                                        .width(6.dp)
                                        .fillMaxHeight()
                                        .background(Color(0xFF005A44))
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reserva.clase.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Hoy, ${reserva.horarioElegido}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (reserva.estado == "Confirmada") Color(0xFFE8F5E9) else Color(0xFFEEEEEE)
                                    ) {
                                        Text(
                                            text = reserva.estado,
                                            color = if (reserva.estado == "Confirmada") Color(0xFF2E7D32) else Color(0xFF757575),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                if (reserva.estado == "Confirmada") {
                                    IconButton(onClick = { reservaAEliminar = reserva }) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Cancelar reserva",
                                            tint = Color(0xFFD32F2F)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación
        reservaAEliminar?.let { reserva ->
            AlertDialog(
                onDismissRequest = { reservaAEliminar = null },
                title = { Text(text = "Cancelar reserva") },
                text = {
                    Text(text = "¿Estás seguro de que deseas cancelar la reserva para la clase de ${reserva.clase.nombre}?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onCancelarReserva(reserva)
                            reservaAEliminar = null
                        }
                    ) {
                        Text("Confirmar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { reservaAEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
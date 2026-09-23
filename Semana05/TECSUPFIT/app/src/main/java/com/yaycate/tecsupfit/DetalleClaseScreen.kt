package com.yaycate.tecsupfit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClaseScreen(
    clase: ClaseGym,
    onBack: () -> Unit,
    onReservar: (String) -> Unit
) {
    val horarios = listOf("7:00 am", "6:00 pm", "7:30 pm")
    var horarioSeleccionado by remember { mutableStateOf(horarios[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF005A44)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = clase.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${clase.horarioGeneral} - ${clase.sala} · ${clase.duracionMinutos} min",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = clase.descripcion, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF005A44)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Selecciona horario:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(horarios) { horario ->
                        FilterChip(
                            selected = horarioSeleccionado == horario,
                            onClick = { horarioSeleccionado = horario },
                            label = { Text(horario) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF005A44),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            Button(
                onClick = { onReservar(horarioSeleccionado) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005A44))
            ) {
                Text(text = "Reservar cupo", fontWeight = FontWeight.Bold)
            }
        }
    }
}
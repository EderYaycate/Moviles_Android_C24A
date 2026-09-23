package com.yaycate.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InicioScreen(clases: List<ClaseGym>, onClaseClick: (Int) -> Unit) {
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }
    val filtros = listOf("Hoy", "Esta semana")

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("TECSUP Fit", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Elige tu clase de hoy", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(16.dp))

        // LazyRow de chips de filtro (selección única, como un RadioButton)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtros) { filtro ->
                FilterChip(
                    selected = filtroSeleccionado == filtro,
                    onClick = { filtroSeleccionado = filtro },
                    label = { Text(filtro) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // LazyColumn con la lista de clases filtradas
        val clasesFiltradas = clases.filter { it.dia == filtroSeleccionado }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(clasesFiltradas) { clase ->
                ClaseCard(clase = clase, onClick = { onClaseClick(clase.id) })
            }
        }
    }
}

@Composable
fun ClaseCard(clase: ClaseGym, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(clase.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Instructor: ${clase.instructor}", style = MaterialTheme.typography.bodySmall)
            Text("Próximo horario: ${clase.cuposDisponibles.first()}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
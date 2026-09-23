package com.yaycate.clinicasalud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.clinicasalud.data.DataSource
import com.yaycate.clinicasalud.model.Doctor
import androidx.compose.material.icons.filled.Person

@Composable
fun InicioScreen(
    onMedicoClick: (Doctor) -> Unit
) {
    var especialidadSeleccionada by remember { mutableStateOf(DataSource.especialidades.first()) }

    val doctoresFiltrados = if (especialidadSeleccionada == "Todas") {
        DataSource.medicos
    } else {
        DataSource.medicos.filter { it.especialidad == especialidadSeleccionada }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(DataSource.especialidades) { especialidad ->
                FilterChip(
                    selected = especialidad == especialidadSeleccionada,
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(doctoresFiltrados) { doctor ->
                TarjetaDoctor(doctor = doctor, onClick = { onMedicoClick(doctor) })
            }
        }
    }
}

@Composable
fun TarjetaDoctor(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Médico",
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = doctor.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(text = doctor.especialidad, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Star, contentDescription = "Calificación")
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = doctor.calificacion.toString())
            }
        }

    }
}
package com.yaycate.clinicasalud.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawerContent(
    onInicioClick: () -> Unit,
    onMisCitasClick: () -> Unit,
    onHistorialClick: () -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Clínica Salud+",
            style = MaterialTheme.typography.titleLarge,
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        )
        HorizontalDivider()

        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = false,
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = null) },
            onClick = onInicioClick,
            modifier = androidx.compose.ui.Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Mis citas") },
            selected = false,
            icon = { Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = null) },
            onClick = onMisCitasClick,
            modifier = androidx.compose.ui.Modifier.padding(horizontal = 12.dp)
        )

        NavigationDrawerItem(
            label = { Text("Historial médico") },
            selected = false,
            icon = { Icon(imageVector = Icons.Filled.History, contentDescription = null) },
            onClick = onHistorialClick,
            modifier = androidx.compose.ui.Modifier.padding(horizontal = 12.dp)
        )
    }
}
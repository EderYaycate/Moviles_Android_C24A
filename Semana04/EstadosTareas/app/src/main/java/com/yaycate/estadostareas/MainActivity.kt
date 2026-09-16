package com.yaycate.estadostareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Tarea(
    val id: Int,
    val nombre: String,
    val completada: Boolean = false
)

enum class Filtro { TODAS, PENDIENTES, COMPLETADAS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                PantallaTareas()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas() {
    val tareas = remember {
        mutableStateListOf(
            Tarea(id = 1, nombre = "Comprar pan"),
            Tarea(id = 2, nombre = "Estudiar Kotlin"),
            Tarea(id = 3, nombre = "Hacer ejercicio")
        )
    }
    var nombreNuevo by remember { mutableStateOf("") }
    var filtroActual by remember { mutableStateOf(Filtro.TODAS) }

    val completadas = tareas.count { it.completada }
    val pendientes = tareas.size - completadas

    val tareasFiltradas = when (filtroActual) {
        Filtro.TODAS -> tareas
        Filtro.PENDIENTES -> tareas.filter { !it.completada }
        Filtro.COMPLETADAS -> tareas.filter { it.completada }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis tareas",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "$pendientes pendientes · $completadas completadas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = nombreNuevo,
                onValueChange = { nombreNuevo = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (nombreNuevo.isNotBlank()) {
                        val nuevoId = (tareas.maxOfOrNull { it.id } ?: 0) + 1
                        tareas.add(Tarea(id = nuevoId, nombre = nombreNuevo))
                        nombreNuevo = ""
                    }
                }
            ) {
                Text("Agregar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FilterChip(
                selected = filtroActual == Filtro.TODAS,
                onClick = { filtroActual = Filtro.TODAS },
                label = { Text("Todas") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = filtroActual == Filtro.PENDIENTES,
                onClick = { filtroActual = Filtro.PENDIENTES },
                label = { Text("Pendientes") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = filtroActual == Filtro.COMPLETADAS,
                onClick = { filtroActual = Filtro.COMPLETADAS },
                label = { Text("Completadas") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tareasFiltradas, key = { it.id }) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onCambiarEstado = { nuevoEstado ->
                        val index = tareas.indexOf(tarea)
                        tareas[index] = tarea.copy(completada = nuevoEstado)
                    },
                    onEliminar = {
                        tareas.remove(tarea)
                    }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onCambiarEstado: (Boolean) -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarea.completada,
            onCheckedChange = { nuevoEstado -> onCambiarEstado(nuevoEstado) }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = tarea.nombre,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (tarea.completada) {
                TextDecoration.LineThrough
            } else {
                TextDecoration.None
            },
            color = if (tarea.completada) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onEliminar) {
            Text(
                text = "✕",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaTareas() {
    MaterialTheme {
        PantallaTareas()
    }
}
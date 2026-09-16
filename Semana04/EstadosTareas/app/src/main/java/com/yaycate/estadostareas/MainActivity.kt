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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis tareas",
            style = MaterialTheme.typography.headlineSmall
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

        LazyColumn {
            items(tareas) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onCambiarEstado = { nuevoEstado ->
                        val index = tareas.indexOf(tarea)
                        tareas[index] = tarea.copy(completada = nuevoEstado)
                    }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onCambiarEstado: (Boolean) -> Unit
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
            }
        )
    }
}
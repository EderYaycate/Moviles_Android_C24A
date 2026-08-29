package com.yaycate.tarea3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.tarea3.ui.theme.Tarea3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculadoraCuotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Cuotas válidas y su interés asociado
val cuotasPermitidas = mapOf(
    6 to 0.20,
    12 to 0.40,
    24 to 0.60
)

@Composable
fun CalculadoraCuotas(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") }
    var cantidadTexto by remember { mutableStateOf("") }
    var cuotasTexto by remember { mutableStateOf("") }
    var errorCuotas by remember { mutableStateOf<String?>(null) }
    var datosValidados by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Calculadora de Cuotas", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precioTexto,
            onValueChange = { precioTexto = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cantidadTexto,
            onValueChange = { cantidadTexto = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cuotasTexto,
            onValueChange = { cuotasTexto = it },
            label = { Text("N° de cuotas (6, 12 o 24)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val cuotas = cuotasTexto.toIntOrNull()
                if (cuotas == null || !cuotasPermitidas.containsKey(cuotas)) {
                    errorCuotas = "Solo se permiten 6, 12 o 24 cuotas"
                    datosValidados = false
                } else {
                    errorCuotas = null
                    datosValidados = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Validar datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorCuotas?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        if (datosValidados) {
            Text(" Datos válidos. Nombre: $nombre", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
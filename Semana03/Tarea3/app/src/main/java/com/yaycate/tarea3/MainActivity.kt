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
    val preguntas = listOf(
        "Ingrese el nombre del producto:",
        "Ingrese el precio:",
        "Ingrese la cantidad:",
        "Ingrese el número de cuotas (6, 12 o 24):"
    )

    var pasoActual by remember { mutableIntStateOf(0) }
    var inputActual by remember { mutableStateOf("") }
    val historial = remember { mutableStateListOf<String>() }
    var errorTexto by remember { mutableStateOf<String?>(null) }

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableDoubleStateOf(0.0) }
    var cantidad by remember { mutableIntStateOf(0) }
    var cuotas by remember { mutableIntStateOf(0) }

    fun procesarRespuesta() {
        errorTexto = null
        when (pasoActual) {
            0 -> {
                nombre = inputActual
                historial.add("${preguntas[0]} $nombre")
            }
            1 -> {
                val valor = inputActual.toDoubleOrNull()
                if (valor == null) {
                    errorTexto = "Ingrese un precio válido"
                    return
                }
                precio = valor
                historial.add("${preguntas[1]} $precio")
            }
            2 -> {
                val valor = inputActual.toIntOrNull()
                if (valor == null) {
                    errorTexto = "Ingrese una cantidad válida"
                    return
                }
                cantidad = valor
                historial.add("${preguntas[2]} $cantidad")
            }
            3 -> {
                val valor = inputActual.toIntOrNull()
                if (valor == null || !cuotasPermitidas.containsKey(valor)) {
                    errorTexto = "Solo se permiten 6, 12 o 24 cuotas"
                    return
                }
                cuotas = valor
                historial.add("${preguntas[3]} $cuotas")

                val montoInicial = precio * cantidad
                historial.add("")
                historial.add("Monto Inicial: S/ ${"%.2f".format(montoInicial)}")
            }
        }
        inputActual = ""
        if (pasoActual < preguntas.size) {
            pasoActual++
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Calculadora de Cuotas", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            historial.forEach { linea ->
                Text(linea, style = MaterialTheme.typography.bodyMedium)
            }
        }

        errorTexto?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (pasoActual < preguntas.size) {
            Text(preguntas[pasoActual], style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = inputActual,
                onValueChange = { inputActual = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { procesarRespuesta() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}
package com.yaycate.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaycate.mibodega.ui.componentes.BotonPrimario
import com.yaycate.mibodega.ui.theme.BodegaTheme
import com.yaycate.mibodega.ui.theme.VerdeBodega

/**
 * Pantalla 6: Datos de entrega.
 * Guarda solo lo que el usuario escribe; el pedido se confirma hacia arriba (callback).
 */
@Composable
fun DatosEntregaScreen(
    total: Double,
    onVolver: () -> Unit,
    onConfirmarPedido: (direccion: String, referencia: String, telefono: String) -> Unit
) {
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Datos de entrega",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it; mostrarError = false },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = referencia,
                onValueChange = { referencia = it },
                label = { Text("Referencia (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it; mostrarError = false },
                label = { Text("Teléfono de contacto") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            if (mostrarError) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Completa la dirección y el teléfono",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total a pagar", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "S/ %.2f".format(total),
                    style = MaterialTheme.typography.titleMedium,
                    color = VerdeBodega
                )
            }
            Spacer(Modifier.height(16.dp))
            BotonPrimario(
                texto = "Confirmar pedido",
                onClick = {
                    if (direccion.isNotBlank() && telefono.isNotBlank()) {
                        onConfirmarPedido(direccion, referencia, telefono)
                    } else {
                        mostrarError = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DatosEntregaPreview() {
    BodegaTheme {
        DatosEntregaScreen(total = 24.50, onVolver = {}, onConfirmarPedido = { _, _, _ -> })
    }
}
package com.yaycate.mibodega

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    productoId: Int,
    onAgregarAlCarrito: () -> Unit,
    onVolver: () -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Producto #$productoId",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descripción corta del producto. En esta pantalla se detallan los ingredientes, presentación y precio.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Cantidad:", style = MaterialTheme.typography.titleMedium)
                    OutlinedButton(onClick = { if (cantidad > 1) cantidad-- }) {
                        Text("-")
                    }
                    Text(text = "$cantidad", style = MaterialTheme.typography.titleLarge)
                    OutlinedButton(onClick = { cantidad++ }) {
                        Text("+")
                    }
                }
            }

            Button(
                onClick = onAgregarAlCarrito,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}
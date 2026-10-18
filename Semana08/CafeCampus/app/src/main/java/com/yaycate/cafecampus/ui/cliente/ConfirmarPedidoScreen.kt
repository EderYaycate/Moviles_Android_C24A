package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

private val opcionesPago = listOf("Efectivo", "Yape", "Tarjeta")

@Composable
fun ConfirmarPedidoScreen(
    vm: MenuViewModel,
    clienteId: Int,
    onConfirmado: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val carrito by vm.carrito.collectAsState()
    val total by vm.totalCarrito.collectAsState()
    val scope = rememberCoroutineScope()

    var horaRecojo by rememberSaveable { mutableStateOf("") }
    var pago by rememberSaveable { mutableStateOf(opcionesPago.first()) }
    var nota by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(titulo = "Tu pedido", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(relleno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (carrito.isEmpty()) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                carrito.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = item.nombre,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "S/ %.2f".format(item.precio * item.cantidad),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = { vm.cambiarCantidad(item.productoId, item.cantidad - 1) }
                            ) { Text("−") }
                            Text(
                                text = item.cantidad.toString(),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            OutlinedButton(
                                onClick = { vm.cambiarCantidad(item.productoId, item.cantidad + 1) }
                            ) { Text("+") }
                        }
                    }
                    TextButton(onClick = { vm.quitar(item.productoId) }) {
                        Text("Quitar")
                    }
                }

                Text(
                    text = "Total: S/ %.2f".format(total),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Forma de pago",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    opcionesPago.forEach { opcion ->
                        FilterChip(
                            selected = pago == opcion,
                            onClick = { pago = opcion },
                            label = { Text(opcion) }
                        )
                    }
                }

                CafeTextField(
                    valor = horaRecojo,
                    onValorChange = { horaRecojo = it },
                    etiqueta = "Hora de recojo (ej. 13:30)",
                    modifier = Modifier.fillMaxWidth()
                )
                CafeTextField(
                    valor = nota,
                    onValorChange = { nota = it },
                    etiqueta = "Nota (opcional)",
                    modifier = Modifier.fillMaxWidth()
                )

                CafeButton(
                    texto = "Confirmar pedido",
                    enabled = horaRecojo.isNotBlank() && clienteId > 0,
                    onClick = {
                        scope.launch {
                            val id = vm.confirmarPedido(
                                clienteId = clienteId,
                                horaRecojo = horaRecojo.trim(),
                                pago = pago,
                                nota = nota.trim()
                            )
                            onConfirmado(id.toInt())
                        }
                    }
                )
            }
        }
    }
}
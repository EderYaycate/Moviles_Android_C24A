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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

private val opcionesPago = listOf("Pagar en caja", "Yape / Plin")
private val opcionesHora = listOf("Ahora", "10:30", "11:00", "11:30")

@Composable
fun ConfirmarPedidoScreen(
    vm: MenuViewModel,
    clienteId: Int,
    recibirPedidos: Boolean,
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
        topBar = { AppTopBar(titulo = "Confirmar pedido", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(relleno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!recibirPedidos) {
                Text(
                    text = "Cafetería cerrada por ahora. No se pueden hacer pedidos.",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (carrito.isEmpty()) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir al menú")
                }
            } else {
                carrito.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.nombre,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "S/ %.2f".format(item.precio * item.cantidad),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (item.nota.isNotBlank()) {
                                Text(
                                    text = "Nota: ${item.nota}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
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
                    text = "Hora de recojo",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    opcionesHora.forEach { hora ->
                        FilterChip(
                            selected = horaRecojo == hora,
                            onClick = { horaRecojo = hora },
                            label = { Text(hora) }
                        )
                    }
                }

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
                    valor = nota,
                    onValorChange = { nota = it },
                    etiqueta = "Nota general (opcional)",
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                        Text("S/ %.2f".format(total), style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Servicio", style = MaterialTheme.typography.bodyMedium)
                        Text("S/ 0.00", style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "S/ %.2f".format(total),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                CafeButton(
                    texto = "Confirmar pedido",
                    enabled = recibirPedidos && horaRecojo.isNotBlank() && clienteId > 0,
                    onClick = {
                        scope.launch {
                            val id = vm.confirmarPedido(
                                clienteId = clienteId,
                                horaRecojo = horaRecojo,
                                pago = pago,
                                nota = nota.trim()
                            )
                            onConfirmado(id.toInt())
                        }
                    }
                )

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seguir pidiendo")
                }
            }
        }
    }
}
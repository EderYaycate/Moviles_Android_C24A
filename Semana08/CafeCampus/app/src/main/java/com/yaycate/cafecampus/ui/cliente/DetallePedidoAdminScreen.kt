package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.StepperEstado
import com.yaycate.cafecampus.ui.theme.EstadoListo
import com.yaycate.cafecampus.ui.theme.EstadoPreparando
import com.yaycate.cafecampus.ui.theme.EstadoRecibido
import com.yaycate.cafecampus.viewmodel.PedidosAdminViewModel

private val estadosCambio = listOf(
    EstadoPedido.RECIBIDO,
    EstadoPedido.PREPARANDO,
    EstadoPedido.LISTO,
    EstadoPedido.ENTREGADO
)

@Composable
fun DetallePedidoAdminScreen(
    vm: PedidosAdminViewModel,
    pedidoId: Int,
    onVolver: () -> Unit
) {
    val flujo = remember(pedidoId) { vm.observarPedido(pedidoId) }
    val pedidoCon by flujo.collectAsState(initial = null)

    Scaffold(
        topBar = { AppTopBar(titulo = "Detalle del pedido", onVolver = onVolver) }
    ) { relleno ->
        val actual = pedidoCon
        if (actual == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando pedido...", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            val estado = estadoDe(actual.pedido.estado)
            val siguiente = estadoSiguiente(estado)
            val puedeCancelar = estado != EstadoPedido.ENTREGADO && estado != EstadoPedido.CANCELADO

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Encabezado del pedido
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Pedido #${actual.pedido.id}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Recojo: ${actual.pedido.horaRecojo} · Pago: ${actual.pedido.pago}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        EstadoBadge(estado = estado)
                    }
                }

                StepperEstado(estado = estado)

                // Cliente
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Column {
                            Text(
                                text = "Cliente",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = actual.cliente.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Estudiante",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Productos y total
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        actual.detalles.forEach { detalle ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${detalle.nombreProducto}  x${detalle.cantidad}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "S/ %.2f".format(detalle.precioUnitario * detalle.cantidad),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "S/ %.2f".format(actual.pedido.total),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Nota del cliente
                if (actual.pedido.nota.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFEF3C7)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nota del cliente",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = actual.pedido.nota,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }

                // Cambiar estado
                Text(
                    text = "Cambiar estado",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    estadosCambio.forEach { e ->
                        FilterChip(
                            selected = estado == e,
                            onClick = { if (estado != e) vm.cambiarEstado(pedidoId, e) },
                            label = { Text(etiqueta(e)) }
                        )
                    }
                }

                if (siguiente != null) {
                    Button(
                        onClick = { vm.cambiarEstado(pedidoId, siguiente) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pasar a ${etiqueta(siguiente)}")
                    }
                }

                if (puedeCancelar) {
                    OutlinedButton(
                        onClick = { vm.cambiarEstado(pedidoId, EstadoPedido.CANCELADO) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar pedido", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
private fun EstadoBadge(estado: EstadoPedido) {
    val color = colorDeEstado(estado)
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = etiqueta(estado),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private fun colorDeEstado(e: EstadoPedido): Color = when (e) {
    EstadoPedido.RECIBIDO -> EstadoRecibido
    EstadoPedido.PREPARANDO -> EstadoPreparando
    EstadoPedido.LISTO -> EstadoListo
    EstadoPedido.ENTREGADO -> Color(0xFF6B7280)
    EstadoPedido.CANCELADO -> Color(0xFFDC2626)
}

private fun estadoDe(texto: String): EstadoPedido =
    runCatching { EstadoPedido.valueOf(texto) }.getOrDefault(EstadoPedido.RECIBIDO)

private fun estadoSiguiente(estado: EstadoPedido): EstadoPedido? = when (estado) {
    EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
    EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
    EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
    EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO -> null
}

private fun etiqueta(e: EstadoPedido): String = when (e) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}
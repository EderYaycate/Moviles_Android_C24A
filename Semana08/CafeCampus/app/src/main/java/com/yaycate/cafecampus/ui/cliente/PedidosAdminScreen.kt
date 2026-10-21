package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.obtenerIniciales
import com.yaycate.cafecampus.ui.theme.EstadoListo
import com.yaycate.cafecampus.ui.theme.EstadoPreparando
import com.yaycate.cafecampus.ui.theme.EstadoRecibido
import com.yaycate.cafecampus.viewmodel.PedidosAdminViewModel

private val pestanasAdmin = listOf(
    EstadoPedido.RECIBIDO,
    EstadoPedido.PREPARANDO,
    EstadoPedido.LISTO
)

private val etiquetasPestana = listOf("Nuevos", "Preparando", "Listos")

@Composable
fun PedidosAdminScreen(
    vm: PedidosAdminViewModel,
    onPedidoClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val estado by vm.estado.collectAsState()
    val pedidos by vm.pedidos.collectAsState()
    val indice = pestanasAdmin.indexOf(estado).coerceAtLeast(0)

    Scaffold(
        topBar = { AppTopBar(titulo = "Pedidos", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pestanasAdmin.forEachIndexed { i, e ->
                    FilterChip(
                        selected = indice == i,
                        onClick = { vm.filtrar(e) },
                        label = { Text(etiquetasPestana[i]) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (pedidos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos en esta pestaña",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidos, key = { it.pedido.id }) { item ->
                        TarjetaPedidoAdmin(
                            item = item,
                            onClick = { onPedidoClick(item.pedido.id) },
                            onPasarSiguiente = { nuevo -> vm.cambiarEstado(item.pedido.id, nuevo) },
                            onCancelar = { vm.cambiarEstado(item.pedido.id, EstadoPedido.CANCELADO) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedidoAdmin(
    item: PedidoConDetalle,
    onClick: () -> Unit,
    onPasarSiguiente: (EstadoPedido) -> Unit,
    onCancelar: () -> Unit
) {
    val pedido = item.pedido
    val actual = estadoDe(pedido.estado)
    val siguiente = siguienteEstado(actual)
    val puedeCancelar = actual != EstadoPedido.ENTREGADO && actual != EstadoPedido.CANCELADO
    val resumen = item.detalles.joinToString(" + ") { "${it.cantidad} x ${it.nombreProducto}" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = obtenerIniciales(item.cliente.nombre),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "#${pedido.id} ${item.cliente.nombre}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = resumen,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                EstadoBadge(estado = actual)
            }

            Text(
                text = "Recojo: ${pedido.horaRecojo} · S/ %.2f".format(pedido.total),
                style = MaterialTheme.typography.bodyMedium
            )

            if (pedido.nota.isNotBlank()) {
                Text(
                    text = "Nota: ${pedido.nota}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (siguiente != null || puedeCancelar) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (siguiente != null) {
                        Button(
                            onClick = { onPasarSiguiente(siguiente) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Pasar a ${etiqueta(siguiente)}")
                        }
                    }
                    if (puedeCancelar) {
                        Button(
                            onClick = onCancelar,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Cancelar")
                        }
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

private fun siguienteEstado(actual: EstadoPedido): EstadoPedido? = when (actual) {
    EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
    EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
    EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
    else -> null
}

private fun etiqueta(e: EstadoPedido): String = when (e) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}
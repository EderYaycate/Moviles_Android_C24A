package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.relation.PedidoConDetalle
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun PedidosAdminScreen(
    vm: PedidosAdminViewModel,
    onPedidoClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val estado by vm.estado.collectAsState()
    val pedidos by vm.pedidos.collectAsState()

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
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadoPedido.entries.forEach { e ->
                    FilterChip(
                        selected = estado == e,
                        onClick = { vm.filtrar(e) },
                        label = { Text(etiqueta(e)) }
                    )
                }
            }

            if (pedidos.isEmpty()) {
                Text(
                    text = "No hay pedidos en este estado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Pedido #${pedido.id} · ${item.cliente.nombre}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Estado: ${etiqueta(actual)} · Recojo: ${pedido.horaRecojo}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Total: S/ %.2f".format(pedido.total),
                style = MaterialTheme.typography.bodyMedium
            )
            if (!pedido.nota.isNullOrBlank()) {
                Text(
                    text = "Nota: ${pedido.nota}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (siguiente != null) {
                CafeButton(
                    texto = "Pasar a ${etiqueta(siguiente)}",
                    onClick = { onPasarSiguiente(siguiente) }
                )
            }

            if (puedeCancelar) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar pedido")
                }
            }
        }
    }
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
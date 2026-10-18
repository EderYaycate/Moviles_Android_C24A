package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.StepperEstado
import com.yaycate.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun DetallePedidoAdminScreen(
    vm: PedidosAdminViewModel,
    pedidoId: Int,
    onVolver: () -> Unit
) {
    val flujo = remember(pedidoId) { vm.observarPedido(pedidoId) }
    val pedidoCon by flujo.collectAsState(initial = null)

    Scaffold(
        topBar = { AppTopBar(titulo = "Pedido #$pedidoId", onVolver = onVolver) }
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
            val estado = EstadoPedido.valueOf(actual.pedido.estado)
            val siguiente = estadoSiguiente(estado)
            val puedeCancelar = estado != EstadoPedido.ENTREGADO && estado != EstadoPedido.CANCELADO

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Cliente: ${actual.cliente.nombre}",
                    style = MaterialTheme.typography.titleMedium
                )

                StepperEstado(estado = estado)

                Text(
                    text = "Recojo: ${actual.pedido.horaRecojo} · Pago: ${actual.pedido.pago}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Detalle",
                    style = MaterialTheme.typography.titleSmall
                )
                actual.detalles.forEach { detalle ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${detalle.cantidad} x ${detalle.nombreProducto}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "S/ %.2f".format(detalle.precioUnitario * detalle.cantidad),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Text(
                    text = "Total: S/ %.2f".format(actual.pedido.total),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                if (actual.pedido.nota.isNotBlank()) {
                    Text(
                        text = "Nota: ${actual.pedido.nota}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (siguiente != null) {
                    CafeButton(
                        texto = "Pasar a ${nombreEstado(siguiente)}",
                        onClick = { vm.cambiarEstado(pedidoId, siguiente) }
                    )
                }

                if (puedeCancelar) {
                    OutlinedButton(
                        onClick = { vm.cambiarEstado(pedidoId, EstadoPedido.CANCELADO) }
                    ) {
                        Text("Cancelar pedido", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

private fun estadoSiguiente(estado: EstadoPedido): EstadoPedido? = when (estado) {
    EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
    EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
    EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
    EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO -> null
}

private fun nombreEstado(estado: EstadoPedido): String = when (estado) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}
package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.PedidoCard
import com.yaycate.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun PedidosAdminScreen(
    vm: PedidosAdminViewModel,
    onVolver: () -> Unit
) {
    val pedidos by vm.pedidos.collectAsState()
    val estado by vm.estado.collectAsState()

    Scaffold(
        topBar = { AppTopBar(titulo = "Gestionar pedidos", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadoPedido.entries.forEach { opcion ->
                    FilterChip(
                        selected = estado == opcion,
                        onClick = { vm.filtrar(opcion) },
                        label = { Text(etiquetaEstado(opcion)) }
                    )
                }
            }

            if (pedidos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos en este estado",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidos, key = { it.pedido.id }) { item ->
                        val actual = EstadoPedido.valueOf(item.pedido.estado)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PedidoCard(pedido = item.pedido, onClick = {})
                            siguienteEstado(actual)?.let { sig ->
                                CafeButton(
                                    texto = "Pasar a ${etiquetaEstado(sig)}",
                                    onClick = { vm.cambiarEstado(item.pedido.id, sig) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun siguienteEstado(estado: EstadoPedido): EstadoPedido? = when (estado) {
    EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
    EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
    EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
    EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO -> null
}

private fun etiquetaEstado(estado: EstadoPedido): String = when (estado) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}
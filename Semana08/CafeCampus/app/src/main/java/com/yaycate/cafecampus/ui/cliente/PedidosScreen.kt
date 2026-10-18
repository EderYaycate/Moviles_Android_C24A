package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.PedidoCard
import com.yaycate.cafecampus.viewmodel.PedidosClienteViewModel

private val estadosActivos = setOf(
    EstadoPedido.RECIBIDO,
    EstadoPedido.PREPARANDO,
    EstadoPedido.LISTO
)

@Composable
fun PedidosScreen(
    vm: PedidosClienteViewModel,
    clienteId: Int,
    onPedidoClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val pedidos by vm.pedidos.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(clienteId) {
        if (clienteId > 0) vm.cargar(clienteId)
    }

    val mostrados = pedidos.filter { item ->
        val activo = estadoDe(item.pedido.estado) in estadosActivos
        if (pestana == 0) activo else !activo
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Mis pedidos", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
        ) {
            TabRow(selectedTabIndex = pestana) {
                Tab(
                    selected = pestana == 0,
                    onClick = { pestana = 0 },
                    text = { Text("Activos") }
                )
                Tab(
                    selected = pestana == 1,
                    onClick = { pestana = 1 },
                    text = { Text("Historial") }
                )
            }

            if (mostrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pestana == 0) {
                            "No tienes pedidos activos"
                        } else {
                            "Aún no tienes pedidos en el historial"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mostrados, key = { it.pedido.id }) { item ->
                        PedidoCard(
                            pedido = item.pedido,
                            onClick = { onPedidoClick(item.pedido.id) }
                        )
                    }
                }
            }
        }
    }
}

private fun estadoDe(texto: String): EstadoPedido =
    runCatching { EstadoPedido.valueOf(texto) }.getOrDefault(EstadoPedido.RECIBIDO)
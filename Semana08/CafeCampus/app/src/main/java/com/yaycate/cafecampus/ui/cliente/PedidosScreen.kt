package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.PedidoCard
import com.yaycate.cafecampus.viewmodel.PedidosClienteViewModel

@Composable
fun PedidosScreen(
    vm: PedidosClienteViewModel,
    clienteId: Int,
    onPedidoClick: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val pedidos by vm.pedidos.collectAsState()

    LaunchedEffect(clienteId) {
        if (clienteId > 0) vm.cargar(clienteId)
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Mis pedidos", onVolver = onVolver) }
    ) { relleno ->
        if (pedidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes pedidos",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { item ->
                    PedidoCard(
                        pedido = item.pedido,
                        onClick = { onPedidoClick(item.pedido.id) }
                    )
                }
            }
        }
    }
}
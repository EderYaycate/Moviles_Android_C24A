package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.theme.EstadoListo
import com.yaycate.cafecampus.ui.theme.EstadoPreparando
import com.yaycate.cafecampus.ui.theme.EstadoRecibido
import com.yaycate.cafecampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerPedidos: () -> Unit,
    onGestionarMenu: () -> Unit
) {
    val ventas by vm.ventasHoy.collectAsState()
    val recibidos by vm.recibidos.collectAsState()
    val preparando by vm.preparando.collectAsState()
    val listos by vm.listos.collectAsState()
    val nuevos by vm.nuevos.collectAsState()

    Scaffold(
        topBar = { AppTopBar(titulo = "Panel") }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaResumen(
                titulo = "Ventas de hoy",
                valor = "S/ %.2f".format(ventas),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TarjetaResumen(
                    titulo = "Recibidos",
                    valor = recibidos.toString(),
                    modifier = Modifier.weight(1f),
                    colorValor = EstadoRecibido
                )
                TarjetaResumen(
                    titulo = "Preparando",
                    valor = preparando.toString(),
                    modifier = Modifier.weight(1f),
                    colorValor = EstadoPreparando
                )
                TarjetaResumen(
                    titulo = "Listos",
                    valor = listos.toString(),
                    modifier = Modifier.weight(1f),
                    colorValor = EstadoListo
                )
            }

            Text(
                text = "Nuevos pedidos",
                style = MaterialTheme.typography.titleMedium
            )

            if (nuevos.isEmpty()) {
                Text(
                    text = "No hay pedidos nuevos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            nuevos.take(5).forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Pedido #${item.pedido.id} · ${item.cliente.nombre}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Recojo: ${item.pedido.horaRecojo} · S/ %.2f".format(item.pedido.total),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        CafeButton(
                            texto = "Preparar",
                            onClick = { vm.preparar(item.pedido.id) }
                        )
                    }
                }
            }

            CafeButton(
                texto = "Gestionar pedidos",
                onClick = onVerPedidos
            )

            CafeButton(
                texto = "Gestionar menú",
                onClick = onGestionarMenu
            )
        }
    }
}

@Composable
private fun TarjetaResumen(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    colorValor: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall,
                color = colorValor
            )
        }
    }
}
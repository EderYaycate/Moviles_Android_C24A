package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    val cantidadHoy by vm.cantidadHoy.collectAsState()
    val recibidos by vm.recibidos.collectAsState()
    val preparando by vm.preparando.collectAsState()
    val listos by vm.listos.collectAsState()
    val nuevos by vm.nuevos.collectAsState()
    val porHora by vm.pedidosPorHora.collectAsState()

    Scaffold(
        topBar = { AppTopBar(titulo = "Panel de control") }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TarjetaEstado(
                    titulo = "Nuevos",
                    valor = recibidos,
                    color = EstadoRecibido,
                    modifier = Modifier.weight(1f)
                )
                TarjetaEstado(
                    titulo = "Preparando",
                    valor = preparando,
                    color = EstadoPreparando,
                    modifier = Modifier.weight(1f)
                )
                TarjetaEstado(
                    titulo = "Listos",
                    valor = listos,
                    color = EstadoListo,
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
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
                            text = "Ventas del día",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "S/ %.2f".format(ventas),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = "$cantidadHoy pedidos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Text(
                text = "Pedidos por hora",
                style = MaterialTheme.typography.titleMedium
            )
            GraficoPorHora(datos = porHora)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevos pedidos",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onVerPedidos) {
                    Text("Ver todos")
                }
            }

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pedido #${item.pedido.id} · ${item.cliente.nombre}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Recojo: ${item.pedido.horaRecojo} · S/ %.2f".format(item.pedido.total),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Button(onClick = { vm.preparar(item.pedido.id) }) {
                            Text("Preparar")
                        }
                    }
                }
            }

            CafeButton(
                texto = "Gestionar menú",
                onClick = onGestionarMenu
            )
        }
    }
}

@Composable
private fun TarjetaEstado(
    titulo: String,
    valor: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun GraficoPorHora(datos: List<com.yaycate.cafecampus.viewmodel.VentaHora>) {
    val maximo = (datos.maxOfOrNull { it.cantidad } ?: 0).coerceAtLeast(1)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            datos.forEach { venta ->
                val alto = if (venta.cantidad == 0) 4 else maxOf(12, 90 * venta.cantidad / maximo)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = venta.cantidad.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(alto.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                    Text(
                        text = "${venta.hora}h",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerPedidos: () -> Unit,
    onGestionarMenu: () -> Unit,
    onAjustes: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val ventas by vm.ventasHoy.collectAsState()
    val recibidos by vm.recibidos.collectAsState()
    val preparando by vm.preparando.collectAsState()
    val listos by vm.listos.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Panel",
                acciones = {
                    IconButton(onClick = onAjustes) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes"
                        )
                    }
                    TextButton(onClick = onCerrarSesion) { Text("Salir") }
                }
            )
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp),
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
                    modifier = Modifier.weight(1f)
                )
                TarjetaResumen(
                    titulo = "Preparando",
                    valor = preparando.toString(),
                    modifier = Modifier.weight(1f)
                )
                TarjetaResumen(
                    titulo = "Listos",
                    valor = listos.toString(),
                    modifier = Modifier.weight(1f)
                )
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
    modifier: Modifier = Modifier
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
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
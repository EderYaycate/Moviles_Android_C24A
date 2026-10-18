package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.prefs.PreferenciasStore
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import kotlinx.coroutines.launch

@Composable
fun AjustesScreen(
    prefs: PreferenciasStore,
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit
) {
    val modoOscuro by prefs.modoOscuro.collectAsState(initial = false)
    val recibirPedidos by prefs.recibirPedidos.collectAsState(initial = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { AppTopBar(titulo = "Ajustes", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilaAjuste(
                titulo = "Modo oscuro",
                subtitulo = "Usa colores oscuros en toda la app",
                checked = modoOscuro,
                onCambio = { valor ->
                    scope.launch { prefs.guardarModoOscuro(valor) }
                }
            )

            FilaAjuste(
                titulo = "Recibir avisos de pedidos",
                subtitulo = "Muestra avisos cuando cambie tu pedido",
                checked = recibirPedidos,
                onCambio = { valor ->
                    scope.launch { prefs.guardarRecibirPedidos(valor) }
                }
            )

            CafeButton(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion
            )
        }
    }
}

@Composable
private fun FilaAjuste(
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCambio: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCambio
        )
    }
}
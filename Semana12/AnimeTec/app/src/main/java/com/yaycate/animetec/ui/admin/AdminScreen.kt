package com.yaycate.animetec.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.ui.UiState

@Composable
fun AdminRoute(viewModel: AdminViewModel, modifier: Modifier = Modifier) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val ocupado by viewModel.ocupado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    // Se recargan los totales cada vez que se entra (los favoritos cambian desde otras pantallas)
    LaunchedEffect(Unit) { viewModel.cargarTotales() }

    AdminScreen(
        estado = estado,
        ocupado = ocupado,
        aviso = aviso,
        onSincronizar = viewModel::sincronizar,
        onLimpiar = viewModel::limpiarCache,
        modifier = modifier
    )
}

@Composable
fun AdminScreen(
    estado: UiState<AdminResumen>,
    ocupado: Boolean,
    aviso: String?,
    onSincronizar: () -> Unit,
    onLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Panel Admin", style = MaterialTheme.typography.headlineMedium)

        when (estado) {
            is UiState.Cargando -> CircularProgressIndicator()
            is UiState.Error -> Text(estado.mensaje, color = MaterialTheme.colorScheme.error)
            is UiState.Exito -> {
                TarjetaTotal("Animes guardados", estado.datos.guardados)
                TarjetaTotal("Favoritos", estado.datos.favoritos)
            }
        }

        if (aviso != null) {
            Text(aviso, color = MaterialTheme.colorScheme.primary)
        }

        Button(
            onClick = onSincronizar,
            enabled = !ocupado,
            modifier = Modifier.fillMaxWidth()
        ) { Text(if (ocupado) "Trabajando..." else "Sincronizar") }

        OutlinedButton(
            onClick = onLimpiar,
            enabled = !ocupado,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Limpiar caché") }
    }
}

@Composable
private fun TarjetaTotal(titulo: String, total: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.labelLarge)
            Text(total.toString(), style = MaterialTheme.typography.headlineLarge)
        }
    }
}
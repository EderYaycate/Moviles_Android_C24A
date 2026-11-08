package com.yaycate.animetec.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.domain.model.Novedad
import com.yaycate.animetec.ui.UiState

@Composable
fun AdminRoute(
    viewModel: AdminViewModel,
    novedadesAdminViewModel: NovedadesAdminViewModel,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val ocupado by viewModel.ocupado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    val novedades by novedadesAdminViewModel.novedades.collectAsStateWithLifecycle()
    val publicando by novedadesAdminViewModel.publicando.collectAsStateWithLifecycle()
    val mensajeNovedades by novedadesAdminViewModel.mensaje.collectAsStateWithLifecycle()

    // Se recargan los totales cada vez que se entra (los favoritos cambian desde otras pantallas)
    LaunchedEffect(Unit) { viewModel.cargarTotales() }

    AdminScreen(
        estado = estado,
        ocupado = ocupado,
        aviso = aviso,
        onSincronizar = viewModel::sincronizar,
        onLimpiar = viewModel::limpiarCache,
        novedades = novedades,
        publicando = publicando,
        mensajeNovedades = mensajeNovedades,
        onPublicarNovedad = novedadesAdminViewModel::publicar,
        onEliminarNovedad = novedadesAdminViewModel::eliminar,
        onLimpiarMensajeNovedades = novedadesAdminViewModel::limpiarMensaje,
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
    novedades: List<Novedad> = emptyList(),
    publicando: Boolean = false,
    mensajeNovedades: String? = null,
    onPublicarNovedad: (String, String, String) -> Unit = { _, _, _ -> },
    onEliminarNovedad: (String) -> Unit = {},
    onLimpiarMensajeNovedades: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var mostrarDialogoBorrar by rememberSaveable { mutableStateOf(false) }

    if (mostrarDialogoBorrar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoBorrar = false },
            title = { Text("¿Borrar todos los animes guardados?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoBorrar = false
                        onLimpiar()
                    }
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogoBorrar = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            onClick = { mostrarDialogoBorrar = true },
            enabled = !ocupado,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Limpiar caché") }

        Spacer(Modifier.height(8.dp))
        Divider()
        Spacer(Modifier.height(8.dp))

        NovedadesAdminSection(
            novedades = novedades,
            publicando = publicando,
            mensaje = mensajeNovedades,
            onPublicar = onPublicarNovedad,
            onEliminar = onEliminarNovedad,
            onLimpiarMensaje = onLimpiarMensajeNovedades
        )
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

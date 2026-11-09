package com.yaycate.animetec.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado con Box de fondo Brush.verticalGradient de primaryContainer a surface y el texto "⚙️ Panel de administración"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Text(
                text = "⚙️ Panel de administración",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (estado) {
                is UiState.Cargando -> CircularProgressIndicator()
                is UiState.Error -> Text(estado.mensaje, color = MaterialTheme.colorScheme.error)
                is UiState.Exito -> {
                    // Totales (guardados y favoritos) en dos ElevatedCard lado a lado con emoji grande (📚 y ❤️), número en headlineLarge y etiqueta pequeña
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaTotal(
                            emoji = "📚",
                            titulo = "Animes guardados",
                            total = estado.datos.guardados,
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaTotal(
                            emoji = "❤️",
                            titulo = "Favoritos",
                            total = estado.datos.favoritos,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (aviso != null) {
                Text(aviso, color = MaterialTheme.colorScheme.primary)
            }

            // Botón Sincronizar con ícono Refresh y colores primary
            Button(
                onClick = onSincronizar,
                enabled = !ocupado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (ocupado) "Trabajando..." else "Sincronizar")
            }

            // Limpiar caché como OutlinedButton con color de error
            OutlinedButton(
                onClick = { mostrarDialogoBorrar = true },
                enabled = !ocupado,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Limpiar caché")
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))

            NovedadesAdminSection(
                novedades = novedades,
                publicando = publicando,
                mensaje = mensajeNovedades,
                onPublicar = onPublicarNovedad,
                onEliminar = onEliminarNovedad,
                onLimpiarMensaje = onLimpiarMensajeNovedades
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaTotal(emoji: String, titulo: String, total: Int, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(emoji, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(total.toString(), style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(4.dp))
            Text(titulo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

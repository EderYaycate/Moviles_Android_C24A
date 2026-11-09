package com.yaycate.animetec.ui.admin

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.animetec.domain.model.Novedad

@Composable
fun NovedadesAdminSection(
    novedades: List<Novedad>,
    publicando: Boolean,
    mensaje: String?,
    onPublicar: (String, String, String) -> Unit,
    onEliminar: (String) -> Unit,
    onLimpiarMensaje: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var mensajeInput by rememberSaveable { mutableStateOf("") }
    var animeIdTexto by rememberSaveable { mutableStateOf("") }
    var novedadAEliminar by remember { mutableStateOf<String?>(null) }

    if (novedadAEliminar != null) {
        AlertDialog(
            onDismissRequest = { novedadAEliminar = null },
            title = { Text("¿Eliminar esta novedad?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        novedadAEliminar?.let { onEliminar(it) }
                        novedadAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { novedadAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("📢 Publicar novedad", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { if (it.length <= 60) titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    enabled = !publicando,
                    shape = RoundedCornerShape(16.dp),
                    supportingText = { Text("${titulo.length}/60") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = mensajeInput,
                    onValueChange = { if (it.length <= 300) mensajeInput = it },
                    label = { Text("Mensaje") },
                    minLines = 3,
                    enabled = !publicando,
                    shape = RoundedCornerShape(16.dp),
                    supportingText = { Text("${mensajeInput.length}/300") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = animeIdTexto,
                    onValueChange = { val filtered = it.filter { ch -> ch.isDigit() }; if (filtered.length <= 10) animeIdTexto = filtered },
                    label = { Text("ID del anime (Opcional)") },
                    singleLine = true,
                    enabled = !publicando,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = { Text("Opcional: abre el detalle de ese anime") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (mensaje != null) {
                    Text(
                        text = mensaje,
                        color = if (mensaje == "Novedad publicada" || mensaje == "Novedad eliminada") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                val primary = MaterialTheme.colorScheme.primary
                val secondary = MaterialTheme.colorScheme.secondary
                val pubBrush = Brush.horizontalGradient(
                    colors = listOf(primary.copy(alpha = 0.9f), secondary.copy(alpha = 0.9f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(pubBrush)
                        .clickable(enabled = !publicando && titulo.isNotBlank() && mensajeInput.isNotBlank()) {
                            onPublicar(titulo, mensajeInput, animeIdTexto)
                            titulo = ""
                            mensajeInput = ""
                            animeIdTexto = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (publicando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Publicar",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Text("📋 Publicadas", style = MaterialTheme.typography.titleMedium)

        if (novedades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📭", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Aún no has publicado novedades",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(novedades, key = { it.id }) { novedad ->
                    val tiempoRelativo = remember(novedad.fecha) {
                        DateUtils.getRelativeTimeSpanString(
                            novedad.fecha,
                            System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS
                        ).toString()
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🔔", fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = novedad.titulo, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(text = novedad.mensaje, style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = tiempoRelativo,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { novedadAEliminar = novedad.id }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

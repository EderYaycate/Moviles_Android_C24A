package com.yaycate.animetec.ui.novedades

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovedadesRoute(
    viewModel: NovedadesViewModel,
    onVolver: () -> Unit,
    onVerAnime: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.marcarVistas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🔔 Novedades") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        NovedadesScreen(
            uiState = uiState,
            onVerAnime = onVerAnime,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun NovedadesScreen(
    uiState: NovedadesUiState,
    onVerAnime: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        uiState.error?.let { err ->
            val errorMessage = if (err.contains("PERMISSION_DENIED", ignoreCase = true) || err.contains("permission", ignoreCase = true)) {
                "No tienes permiso para ver las novedades. Revisa las reglas de Firestore"
            } else {
                "No se pudieron cargar las novedades"
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (uiState.novedades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(text = "📭", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Aún no hay novedades",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.novedades, key = { it.id }) { novedad ->
                    val esNueva = novedad.fecha > uiState.ultimaVistaAnterior
                    val tiempoRelativo = remember(novedad.fecha) {
                        DateUtils.getRelativeTimeSpanString(
                            novedad.fecha,
                            System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS
                        ).toString()
                    }

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val primary = MaterialTheme.colorScheme.primary
                            val secondary = MaterialTheme.colorScheme.secondary
                            val circleBrush = Brush.linearGradient(listOf(primary, secondary))

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(circleBrush),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = novedad.titulo,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    if (esNueva) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ) {
                                            Text("Nuevo")
                                        }
                                    }
                                }

                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = novedad.mensaje,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(tiempoRelativo) },
                                        modifier = Modifier.height(26.dp)
                                    )

                                    novedad.animeId?.let { animeId ->
                                        FilledTonalButton(
                                            onClick = { onVerAnime(animeId) },
                                            modifier = Modifier.height(32.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                        ) {
                                            Text("Ver anime")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.yaycate.animetec.ui.favoritos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.ui.UiState
import com.yaycate.animetec.ui.components.Cargando
import com.yaycate.animetec.ui.components.ErrorView
import com.yaycate.animetec.ui.lista.BuscadorAnime
import com.yaycate.animetec.ui.lista.ListaScreen
import com.yaycate.animetec.ui.lista.filtrarPorTitulo

@Composable
fun FavoritosRoute(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var consulta by rememberSaveable { mutableStateOf("") }

    when (val e = estado) {
        is UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> ErrorView(
            mensaje = e.mensaje,
            onReintentar = {},
            modifier = modifier
        )
        is UiState.Exito -> {
            val listaFiltrada = remember(e.datos, consulta) {
                e.datos.filtrarPorTitulo(consulta)
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (e.datos.isEmpty()) {
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
                            Text(text = "💔", fontSize = 64.sp)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Aún no tienes favoritos",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Toca el corazón de un anime para guardarlo aquí",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "❤️ Tus favoritos",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${e.datos.size} animes guardados",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    BuscadorAnime(
                        consulta = consulta,
                        onConsultaChange = { consulta = it }
                    )

                    Text(
                        text = "${listaFiltrada.size} animes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    AnimatedVisibility(
                        visible = listaFiltrada.isEmpty() && consulta.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🔍", fontSize = 48.sp)
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = "No encontramos animes con ese nombre",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (listaFiltrada.isNotEmpty()) {
                        ListaScreen(
                            items = listaFiltrada,
                            onItemClick = onItemClick,
                            onFavorito = viewModel::alternarFavorito,
                            consulta = consulta,
                            onConsultaChange = { consulta = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

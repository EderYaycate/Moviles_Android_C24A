package com.yaycate.animetec.ui.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yaycate.animetec.domain.model.Item
import com.yaycate.animetec.ui.UiState
import com.yaycate.animetec.ui.components.Cargando
import com.yaycate.animetec.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRoute(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val traduccion by viewModel.traduccion.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when (val e = estado) {
            is UiState.Cargando -> Cargando(Modifier.padding(padding))
            is UiState.Error -> ErrorView(
                mensaje = e.mensaje,
                onReintentar = viewModel::cargar,
                modifier = Modifier.padding(padding)
            )
            is UiState.Exito -> DetalleContenido(
                item = e.datos,
                traduccion = traduccion,
                onTraducir = viewModel::traducirSinopsis,
                onVerOriginal = viewModel::verOriginal,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DetalleContenido(
    item: Item,
    traduccion: EstadoTraduccion,
    onTraducir: () -> Unit,
    onVerOriginal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = item.imagenUrl,
            contentDescription = item.titulo,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(item.titulo, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(item.subtitulo, style = MaterialTheme.typography.bodyMedium)
        item.puntaje?.let { Text("Puntaje: $it", style = MaterialTheme.typography.bodyMedium) }
        item.estado?.let { Text("Estado: $it", style = MaterialTheme.typography.bodyMedium) }
        item.fechaInicio?.let { Text("Estreno: $it", style = MaterialTheme.typography.bodyMedium) }
        Spacer(Modifier.height(16.dp))
        Text("Sinopsis", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))

        val sinopsisTexto = when (traduccion) {
            is EstadoTraduccion.Traducida -> traduccion.texto
            else -> item.sinopsis ?: "Sin sinopsis disponible"
        }
        Text(sinopsisTexto, style = MaterialTheme.typography.bodyMedium)

        if (!item.sinopsis.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            when (traduccion) {
                is EstadoTraduccion.Original -> {
                    TextButton(onClick = onTraducir) {
                        Text("Traducir al español")
                    }
                }
                is EstadoTraduccion.Traduciendo -> {
                    TextButton(onClick = {}, enabled = false) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Traduciendo…")
                        }
                    }
                }
                is EstadoTraduccion.Traducida -> {
                    TextButton(onClick = onVerOriginal) {
                        Text("Ver original")
                    }
                }
                is EstadoTraduccion.Fallo -> {
                    Column {
                        Text(
                            text = traduccion.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(4.dp))
                        TextButton(onClick = onTraducir) {
                            Text("Reintentar traducción")
                        }
                    }
                }
            }
        }
    }
}

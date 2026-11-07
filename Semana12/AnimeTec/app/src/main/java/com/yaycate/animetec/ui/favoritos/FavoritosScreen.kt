package com.yaycate.animetec.ui.favoritos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.ui.UiState
import com.yaycate.animetec.ui.components.Cargando
import com.yaycate.animetec.ui.lista.ListaScreen

@Composable
fun FavoritosRoute(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    when (val e = estado) {
        is UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(e.mensaje)
        }
        is UiState.Exito -> {
            if (e.datos.isEmpty()) {
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no tienes favoritos")
                }
            } else {
                ListaScreen(
                    items = e.datos,
                    onItemClick = onItemClick,
                    onFavorito = viewModel::alternarFavorito,
                    modifier = modifier
                )
            }
        }
    }
}
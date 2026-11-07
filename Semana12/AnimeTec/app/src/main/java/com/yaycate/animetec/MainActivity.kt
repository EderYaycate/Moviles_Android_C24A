package com.yaycate.animetec

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yaycate.animetec.data.remote.RetrofitClient
import com.yaycate.animetec.domain.model.Item
import com.yaycate.animetec.ui.lista.ListaScreen
import com.yaycate.animetec.ui.theme.AnimeTecTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeTecTheme {
                // Datos de prueba: se eliminan en H6 cuando conectemos la API
                var items by remember { mutableStateOf(datosDePrueba()) }
                LaunchedEffect(Unit) {
                    try {
                        val r = RetrofitClient.api.listar()
                        Log.d("KITSU", "Recibidos ${r.data.size}: ${r.data.first().attributes.canonicalTitle}")
                    } catch (e: Exception) {
                        Log.e("KITSU", "Error", e)
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaScreen(
                        items = items,
                        onItemClick = { },
                        onFavorito = { id ->
                            items = items.map {
                                if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


private fun datosDePrueba() = listOf(
    Item(
        id = 46474,
        titulo = "Sousou no Frieren",
        subtitulo = "TV · 28 episodios",
        imagenUrl = "https://media.kitsu.app/anime/46474/poster_image/medium-23e1293e41a0b54b6621eb589c3f0d62.jpeg",
        puntaje = "88.86"
    ),
    Item(
        id = 48323,
        titulo = "Chainsaw Man: Reze-hen",
        subtitulo = "Película · 1 episodio",
        imagenUrl = null,
        puntaje = "88.86"
    ),
    Item(
        id = 1,
        titulo = "Cowboy Bebop",
        subtitulo = "TV · 26 episodios",
        imagenUrl = null,
        puntaje = "82.00"
    )
)
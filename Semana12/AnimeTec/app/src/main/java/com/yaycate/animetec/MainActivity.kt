package com.yaycate.animetec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaycate.animetec.ui.ViewModelFactory
import com.yaycate.animetec.ui.lista.ListaRoute
import com.yaycate.animetec.ui.lista.ListaViewModel
import com.yaycate.animetec.ui.theme.AnimeTecTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeTecTheme {
                val container = (application as AnimeApp).container
                val vm: ListaViewModel = viewModel(factory = ViewModelFactory(container))

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaRoute(
                        viewModel = vm,
                        onItemClick = { /* H7: navegar al detalle */ },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
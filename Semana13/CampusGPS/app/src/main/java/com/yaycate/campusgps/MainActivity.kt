package com.yaycate.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaycate.campusgps.ui.PantallaMapa
import com.yaycate.campusgps.ui.theme.CampusGPSTheme
import com.yaycate.campusgps.viewmodel.CampusViewModelFactory
import com.yaycate.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(this))
            val modoOscuro by vm.modoOscuro.collectAsState()
            CampusGPSTheme(darkTheme = modoOscuro) {
                PantallaMapa(vm = vm)
            }
        }
    }
}
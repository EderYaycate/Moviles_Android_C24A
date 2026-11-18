package com.yaycate.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaycate.centrotareas.ui.PantallaTareas
import com.yaycate.centrotareas.ui.theme.CentroTareasTheme
import com.yaycate.centrotareas.viewmodel.TareasViewModel
import com.yaycate.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                val vm: TareasViewModel = viewModel(factory = TareasViewModelFactory())
                PantallaTareas(vm)
            }
        }
    }
}
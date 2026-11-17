package com.yaycate.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaycate.campusgps.model.UbicacionUi
import com.yaycate.campusgps.viewmodel.CampusViewModelFactory
import com.yaycate.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa() {
    val contexto = LocalContext.current
    val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(contexto))
    val estado by vm.estado.collectAsState()

    // TODO Paso 2: lanzador de permisos y alPulsar()

    Scaffold(
        topBar = { TopAppBar(title = { Text("CampusGPS") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { vm.buscar() },
                enabled = estado !is UbicacionUi.Buscando
            ) {
                Text("Obtener mi ubicación")
            }

            Spacer(Modifier.height(16.dp))

            when (val e = estado) {
                is UbicacionUi.Lista -> Text("Latitud: ${e.posicion.latitud}\nLongitud: ${e.posicion.longitud}")
                is UbicacionUi.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
                UbicacionUi.Buscando -> Text("Buscando...")
                UbicacionUi.Inicial -> Text("Pulsa el botón para empezar")
            }

            // TODO Paso 3: MapaUbicacion(...)
        }
    }
}
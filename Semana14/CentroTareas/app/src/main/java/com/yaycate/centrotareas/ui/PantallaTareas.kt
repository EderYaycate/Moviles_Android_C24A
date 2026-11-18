package com.yaycate.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yaycate.centrotareas.model.CargaUi
import com.yaycate.centrotareas.model.PrimosUi
import com.yaycate.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val limiteMs by vm.limiteMs.collectAsState()
    val historial by vm.historial.collectAsState()
    var toques by remember { mutableIntStateOf(0) }
    var textoHasta by remember { mutableStateOf("500000") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { toques++ }) {
                Text("Tócame: $toques")
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Tarea con progreso",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { vm.tareaLarga() },
                            enabled = !corriendo
                        ) {
                            Text("Iniciar")
                        }
                        OutlinedButton(
                            onClick = { vm.cancelar() },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${(progreso * 100).toInt()} %")
                    Text(mensaje)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Contar números primos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(
                        value = textoHasta,
                        onValueChange = { textoHasta = it },
                        label = { Text("Contar primos hasta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                textoHasta.toIntOrNull()?.takeIf { it >= 2 }?.let { vm.contar(it) }
                            },
                            enabled = primos !is PrimosUi.Calculando
                        ) {
                            Text("Contar")
                        }
                        OutlinedButton(
                            onClick = { vm.cancelarPrimos() },
                            enabled = primos is PrimosUi.Calculando
                        ) {
                            Text("Cancelar")
                        }
                    }
                    when (val p = primos) {
                        PrimosUi.Inicial -> Unit
                        PrimosUi.Calculando -> Text("Calculando...")
                        is PrimosUi.Listo -> RecuadroResultado(
                            "Primos: ${p.cantidad} en ${p.milisegundos} ms"
                        )
                        PrimosUi.Cancelado -> Text("Cancelado")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Dos cargas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Tiempo máximo")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(2000L, 5000L, 20000L).forEach { ms ->
                            FilterChip(
                                selected = limiteMs == ms,
                                onClick = { vm.cambiarLimite(ms) },
                                label = { Text("${ms / 1000} s") }
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Simular error")
                        Switch(
                            checked = simularError,
                            onCheckedChange = { vm.cambiarSimularError(it) }
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { vm.cargarUnaPorUna() },
                            enabled = carga !is CargaUi.Cargando
                        ) {
                            Text("Una por una")
                        }
                        Button(
                            onClick = { vm.cargarALaVez() },
                            enabled = carga !is CargaUi.Cargando
                        ) {
                            Text("A la vez")
                        }
                    }
                    when (val c = carga) {
                        CargaUi.Inicial -> Unit
                        CargaUi.Cargando -> CircularProgressIndicator()
                        is CargaUi.Exito -> RecuadroResultado(
                            "Modo: ${c.modo}\nPerfil: ${c.perfil}\nPedidos: ${c.pedidos}\nTiempo: ${c.milisegundos} ms"
                        )
                        is CargaUi.Error -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            RecuadroError(c.mensaje)
                            Button(onClick = { vm.cargarALaVez() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Historial de resultados",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (historial.isEmpty()) {
                        Text("Sin resultados todavía")
                    } else {
                        historial.forEach { Text(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecuadroError(texto: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(12.dp),
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun RecuadroResultado(texto: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(12.dp),
            color = Color(0xFF1B5E20)
        )
    }
}

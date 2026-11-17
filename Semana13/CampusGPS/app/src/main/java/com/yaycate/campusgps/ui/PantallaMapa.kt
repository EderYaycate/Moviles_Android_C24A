package com.yaycate.campusgps.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.yaycate.campusgps.model.TipoError
import com.yaycate.campusgps.model.UbicacionUi
import com.yaycate.campusgps.util.formatearDistancia
import com.yaycate.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val contexto = LocalContext.current
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val masCercanoId by vm.masCercanoId.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val modoOscuro by vm.modoOscuro.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->
        val concedido = resultados[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                resultados[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (concedido) {
            vm.buscar()
        } else {
            val activity = contexto as? Activity
            val puedeVolverAPedir = activity?.shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ?: true
            vm.permisoDenegado(permanente = !puedeVolverAPedir)
        }
    }

    fun alPulsar() {
        val tienePermiso =
            ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (tienePermiso) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    fun abrirAjustes(tipo: TipoError) {
        val intent = if (tipo == TipoError.GPS_APAGADO) {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        } else {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", contexto.packageName, null)
            )
        }
        contexto.startActivity(intent)
    }

    val error = estado as? UbicacionUi.Error

    Scaffold(
        topBar = {
            Box(modifier = Modifier.background(DegradadoAzul)) {
                TopAppBar(
                    title = { Text("CampusGPS", color = Color.White) },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = if (modoOscuro) "Oscuro" else "Claro",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = modoOscuro,
                                onCheckedChange = { vm.cambiarModoOscuro(it) }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercanoId,
                esOscuro = modoOscuro,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
            )
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(CircleShape)
                    .background(DegradadoAzul)
            ) {
                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Obtener mi ubicación")
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { vm.irAlMasCercano() },
                enabled = masCercano != null,
                border = BorderStroke(1.dp, AzulCampus),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulCampus),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Ir al más cercano")
            }
            masCercano?.let { cercano ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Más cercano: ")
                        }
                        append("${cercano.lugar.nombre} (${formatearDistancia(cercano.metros)})")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                MensajeError(
                    error = error,
                    onReintentar = { alPulsar() },
                    onAbrirAjustes = { abrirAjustes(error.tipo) }
                )
            }
            Spacer(Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.categorias) { cat ->
                    FilterChip(
                        selected = cat == categoriaSeleccionada,
                        onClick = { vm.seleccionarCategoria(cat) },
                        label = { Text(cat) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercanoId,
                onSeleccionar = { vm.seleccionar(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
            )
        }
    }
}
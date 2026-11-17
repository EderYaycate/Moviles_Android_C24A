package com.yaycate.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yaycate.campusgps.model.LugarConDistancia
import com.yaycate.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0445, -76.9540), 17f)
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val destino = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 17f))
        }
    }

    LaunchedEffect(seleccionado) {
        val item = lugares.find { it.lugar.id == seleccionado }
        if (item != null) {
            val destino = LatLng(item.lugar.latitud, item.lugar.longitud)
            camara.animate(CameraUpdateFactory.newLatLngZoom(destino, 18f))
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = MarkerState(
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Mi ubicación"
            )
        }

        lugares.forEach { item ->
            val esSeleccionado = item.lugar.id == seleccionado
            Marker(
                state = MarkerState(
                    position = LatLng(item.lugar.latitud, item.lugar.longitud)
                ),
                title = item.lugar.nombre,
                icon = BitmapDescriptorFactory.defaultMarker(
                    if (esSeleccionado) BitmapDescriptorFactory.HUE_ORANGE
                    else BitmapDescriptorFactory.HUE_AZURE
                )
            )
        }
    }
}
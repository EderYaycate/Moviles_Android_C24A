package com.yaycate.campusgps.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.yaycate.campusgps.R
import com.yaycate.campusgps.model.LugarConDistancia
import com.yaycate.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val esOscuro = isSystemInDarkTheme()

    val mapProperties = remember(esOscuro) {
        MapProperties(
            mapStyleOptions = if (esOscuro) {
                MapStyleOptions.loadRawResourceStyle(context, R.raw.mapa_oscuro)
            } else {
                null
            }
        )
    }

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
        cameraPositionState = camara,
        properties = mapProperties
    ) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberMarkerState(
                    key = "mi_ubicacion",
                    position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                ),
                title = "Mi ubicación"
            )
        }

        lugares.forEach { item ->
            val color = when {
                item.lugar.id == masCercanoId -> BitmapDescriptorFactory.HUE_GREEN
                item.lugar.id == seleccionado -> BitmapDescriptorFactory.HUE_ORANGE
                else -> BitmapDescriptorFactory.HUE_AZURE
            }
            Marker(
                state = rememberMarkerState(
                    key = "lugar_${item.lugar.id}",
                    position = LatLng(item.lugar.latitud, item.lugar.longitud)
                ),
                title = item.lugar.nombre,
                icon = BitmapDescriptorFactory.defaultMarker(color)
            )
        }
    }
}
package com.yaycate.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yaycate.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(private val context: Context) : UbicacionRepository {

    private val fused = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val disponibilidad = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context)
        if (disponibilidad != ConnectionResult.SUCCESS) throw SinPlayServicesException()

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) throw GpsApagadoException()

        val cancelacion = CancellationTokenSource()
        val ubicacion = fused
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelacion.token)
            .await()
            ?: error("No se pudo obtener la ubicación")

        Posicion(ubicacion.latitude, ubicacion.longitude)
    }
}
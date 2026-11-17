package com.yaycate.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yaycate.campusgps.model.Posicion
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    private val fused = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val cancelacion = CancellationTokenSource()
        val ubicacion = fused
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancelacion.token)
            .await()
            ?: error("No se pudo obtener la ubicación")

        Posicion(ubicacion.latitude, ubicacion.longitude)
    }
}
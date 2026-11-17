package com.yaycate.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.campusgps.model.TipoError
import com.yaycate.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    onAbrirAjustes: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error.mensaje,
            color = MaterialTheme.colorScheme.error
        )
        when (error.tipo) {
            TipoError.PERMISO_PERMANENTE, TipoError.GPS_APAGADO -> {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onAbrirAjustes,
                    border = BorderStroke(1.dp, AzulCampus),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulCampus)
                ) {
                    Text("Abrir ajustes")
                }
            }
            TipoError.PERMISO_DENEGADO, TipoError.OTRO -> {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onReintentar,
                    border = BorderStroke(1.dp, AzulCampus),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulCampus)
                ) {
                    Text("Reintentar")
                }
            }
            TipoError.SIN_PLAY_SERVICES -> Unit
        }
    }
}
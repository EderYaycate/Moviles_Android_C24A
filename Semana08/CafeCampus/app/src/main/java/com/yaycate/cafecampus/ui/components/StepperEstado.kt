package com.yaycate.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.EstadoPedido

private val pasos = listOf(
    EstadoPedido.RECIBIDO to "Recibido",
    EstadoPedido.PREPARANDO to "Preparando",
    EstadoPedido.LISTO to "Listo",
    EstadoPedido.ENTREGADO to "Entregado"
)

@Composable
fun StepperEstado(estado: EstadoPedido, modifier: Modifier = Modifier) {
    if (estado == EstadoPedido.CANCELADO) {
        Text(
            text = "Pedido cancelado",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
        return
    }

    val actual = pasos.indexOfFirst { it.first == estado }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        pasos.forEachIndexed { indice, (_, etiqueta) ->
            val completado = indice <= actual
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (completado) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (completado) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
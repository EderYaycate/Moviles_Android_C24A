package com.yaycate.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.EstadoPedido
import com.yaycate.cafecampus.ui.theme.EstadoCancelado
import com.yaycate.cafecampus.ui.theme.EstadoEntregado
import com.yaycate.cafecampus.ui.theme.EstadoListo
import com.yaycate.cafecampus.ui.theme.EstadoPreparando
import com.yaycate.cafecampus.ui.theme.EstadoRecibido

@Composable
fun EstadoChip(estado: EstadoPedido, modifier: Modifier = Modifier) {
    val color = colorDe(estado)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = textoDe(estado),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

private fun colorDe(estado: EstadoPedido): Color = when (estado) {
    EstadoPedido.RECIBIDO -> EstadoRecibido
    EstadoPedido.PREPARANDO -> EstadoPreparando
    EstadoPedido.LISTO -> EstadoListo
    EstadoPedido.ENTREGADO -> EstadoEntregado
    EstadoPedido.CANCELADO -> EstadoCancelado
}

private fun textoDe(estado: EstadoPedido): String = when (estado) {
    EstadoPedido.RECIBIDO -> "Recibido"
    EstadoPedido.PREPARANDO -> "Preparando"
    EstadoPedido.LISTO -> "Listo"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Cancelado"
}
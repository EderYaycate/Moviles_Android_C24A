package com.yaycate.cafecampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.entity.Pedido
import com.yaycate.cafecampus.model.EstadoPedido
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PedidoCard(
    pedido: Pedido,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onRepetirPedido: (() -> Unit)? = null
) {
    val fechaTexto = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
        .format(Date(pedido.fecha))

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${pedido.id}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                EstadoChip(estado = EstadoPedido.valueOf(pedido.estado))
            }

            Text(
                text = fechaTexto,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recojo: ${pedido.horaRecojo}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "S/ %.2f".format(pedido.total),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (onRepetirPedido != null) {
                Button(
                    onClick = onRepetirPedido,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Repetir pedido")
                }
            }
        }
    }
}
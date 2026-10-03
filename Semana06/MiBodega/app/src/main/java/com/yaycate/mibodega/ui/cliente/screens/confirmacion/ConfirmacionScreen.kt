package com.yaycate.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaycate.mibodega.ui.componentes.BotonPrimario
import com.yaycate.mibodega.ui.componentes.BotonSecundario
import com.yaycate.mibodega.ui.theme.BodegaTheme
import com.yaycate.mibodega.ui.theme.GrisClaro
import com.yaycate.mibodega.ui.theme.VerdeBodega

@Composable
fun ConfirmacionScreen(
    idPedido: String,
    total: Double,
    direccion: String = "Av. Los Olivos 123",
    referencia: String = "Frente al parque",
    onVolverInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Confirmado",
            tint = VerdeBodega,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Pedido realizado!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = VerdeBodega
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu pedido está siendo preparado\ny será entregado pronto.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GrisClaro)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pedido #$idPedido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total: S/ %.2f".format(total),
                    style = MaterialTheme.typography.titleSmall,
                    color = VerdeBodega,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Dirección: $direccion",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (referencia.isNotBlank()) {
                    Text(
                        text = "($referencia)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        BotonSecundario(
            texto = "Ver estado del pedido",
            onClick = { /* TODO: Abrir WhatsApp */ }
        )

        Spacer(modifier = Modifier.height(12.dp))

        BotonPrimario(
            texto = "Volver al inicio",
            onClick = onVolverInicio
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ConfirmacionPreview() {
    BodegaTheme {
        ConfirmacionScreen(
            idPedido = "1024",
            total = 25.90,
            onVolverInicio = {}
        )
    }
}
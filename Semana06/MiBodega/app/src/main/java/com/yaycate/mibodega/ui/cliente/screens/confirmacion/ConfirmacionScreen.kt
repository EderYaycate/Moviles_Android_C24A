package com.yaycate.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.mibodega.ui.cliente.BotonPrincipal
import com.yaycate.mibodega.ui.cliente.FondoTarjeta
import com.yaycate.mibodega.ui.cliente.TextoOscuro
import com.yaycate.mibodega.ui.cliente.VerdeBodega
import com.yaycate.mibodega.ui.cliente.formatoSoles

@Composable

fun ConfirmacionScreen(
    idPedido: String,
    total: Double,
    direccion: String,
    referencia: String,
    onVolverInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = VerdeBodega,
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Pedido realizado!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tu pedido está siendo preparado y será entregado pronto.",
            fontSize = 14.sp,

            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FondoTarjeta, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatoPedido("Pedido", idPedido)
            DatoPedido("Total", formatoSoles(total), destacado = true)
            DatoPedido("Dirección", direccion)
            DatoPedido("Referencia", referencia)
        }

        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(texto = "Volver al inicio", onClick = onVolverInicio)
    }
}

@Composable
private fun DatoPedido(etiqueta: String, valor: String, destacado: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, color = Color.Gray, fontSize = 14.sp)

        Text(
            text = valor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (destacado) VerdeBodega else TextoOscuro
        )
    }
}
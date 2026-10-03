package com.yaycate.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.mibodega.ui.cliente.BotonPrincipal
import com.yaycate.mibodega.ui.cliente.CampoBodega
import com.yaycate.mibodega.ui.cliente.TextoOscuro
import com.yaycate.mibodega.ui.cliente.VerdeBodega
import com.yaycate.mibodega.ui.cliente.formatoSoles

private val metodosPago = listOf("Efectivo al entregar", "Yape", "Plin")

@Composable
fun EntregaScreen(
    total: Double,
    nombreInicial: String = "",
    telefonoInicial: String = "",
    direccionInicial: String = "",
    referenciaInicial: String = "",
    onConfirmarPedido: (String, String) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var telefono by remember { mutableStateOf(telefonoInicial) }
    var direccion by remember { mutableStateOf(direccionInicial) }
    var referencia by remember { mutableStateOf(referenciaInicial) }
    var metodoPago by remember { mutableStateOf(metodosPago.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Datos de entrega",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CampoBodega(nombre, { nombre = it }, "Nombre")
            CampoBodega(telefono, { telefono = it }, "Teléfono", KeyboardType.Phone)
            CampoBodega(direccion, { direccion = it }, "Dirección")
            CampoBodega(referencia, { referencia = it }, "Referencia")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Método de pago",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextoOscuro
        )

        metodosPago.forEach { metodo ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { metodoPago = metodo },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = metodo == metodoPago,
                    onClick = { metodoPago = metodo },
                    colors = RadioButtonDefaults.colors(selectedColor = VerdeBodega)
                )
                Text(metodo, color = TextoOscuro)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total a pagar", fontWeight = FontWeight.Bold, color = TextoOscuro)
            Text(
                text = formatoSoles(total),
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BotonPrincipal(
            texto = "Confirmar pedido",
            onClick = { onConfirmarPedido(direccion, referencia) },
            enabled = direccion.isNotBlank()
        )
    }
}
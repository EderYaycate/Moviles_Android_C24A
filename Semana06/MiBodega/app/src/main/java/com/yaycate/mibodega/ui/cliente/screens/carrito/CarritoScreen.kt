package com.yaycate.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp
import com.yaycate.mibodega.ui.cliente.BotonCantidad
import com.yaycate.mibodega.ui.cliente.BotonPrincipal
import com.yaycate.mibodega.ui.cliente.FondoTarjeta
import com.yaycate.mibodega.ui.cliente.TextoOscuro
import com.yaycate.mibodega.ui.cliente.VerdeBodega
import com.yaycate.mibodega.ui.cliente.formatoSoles
import com.yaycate.mibodega.ui.cliente.modelo.ItemCarrito

@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    subtotal: Double,
    delivery: Double,
    total: Double,
    onIncrementar: (ItemCarrito) -> Unit,
    onDecrementar: (ItemCarrito) -> Unit,
    onEliminar: (ItemCarrito) -> Unit,
    onContinuarPedido: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(

                text = "Mi carrito",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(carrito, key = { it.producto.id }) { item ->
                    FilaCarrito(
                        item = item,
                        onIncrementar = { onIncrementar(item) },
                        onDecrementar = { onDecrementar(item) },
                        onEliminar = { onEliminar(item) }
                    )
                }
            }

        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        FilaResumen("Subtotal", formatoSoles(subtotal), negrita = false)
        FilaResumen("Costo de delivery", formatoSoles(delivery), negrita = false)
        FilaResumen("Total", formatoSoles(total), negrita = true)

        Spacer(modifier = Modifier.height(12.dp))

        BotonPrincipal(
            texto = "Continuar pedido",
            onClick = onContinuarPedido,
            enabled = carrito.isNotEmpty()
        )
    }
}

@Composable
private fun FilaCarrito(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FondoTarjeta, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        item.producto.imagenRes?.let { imagen ->
            Image(
                painter = painterResource(id = imagen),
                contentDescription = item.producto.nombre,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextoOscuro
            )
            Text(text = item.producto.presentacion, fontSize = 12.sp, color = Color.Gray)
            Text(
                text = formatoSoles(item.producto.precio),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }
        BotonCantidad("−", onDecrementar)
        Text(
            text = item.cantidad.toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        BotonCantidad("+", onIncrementar)
        IconButton(onClick = onEliminar) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)

        }
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String, negrita: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (negrita) 18.sp else 14.sp,
            color = TextoOscuro
        )
        Text(
            text = valor,
            fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (negrita) 18.sp else 14.sp,
            color = if (negrita) VerdeBodega else TextoOscuro
        )
    }
}
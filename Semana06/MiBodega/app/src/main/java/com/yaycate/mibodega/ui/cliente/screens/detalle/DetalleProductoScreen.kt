package com.yaycate.mibodega.ui.cliente.screens.detalle

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.yaycate.mibodega.ui.cliente.modelo.Producto

@Composable
fun DetalleScreen(
    producto: Producto,
    onVolver: () -> Unit,
    onAgregarAlCarrito: (Int) -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }
    var favorito by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            IconButton(onClick = { favorito = !favorito }) {
                Icon(
                    imageVector = if (favorito) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (favorito) Color.Red else TextoOscuro
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(FondoTarjeta),
            contentAlignment = Alignment.Center
        ) {
            producto.imagenRes?.let { imagen ->
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = producto.nombre,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,

            color = TextoOscuro
        )
        Text(text = producto.presentacion, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatoSoles(producto.precio),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeBodega
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = producto.descripcion, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonCantidad("−") { if (cantidad > 1) cantidad-- }
            Text(
                text = cantidad.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            BotonCantidad("+") { cantidad++ }
        }

        Spacer(modifier = Modifier.height(16.dp))


        BotonPrincipal(
            texto = "Agregar al carrito",
            onClick = { onAgregarAlCarrito(cantidad) }
        )
    }
}
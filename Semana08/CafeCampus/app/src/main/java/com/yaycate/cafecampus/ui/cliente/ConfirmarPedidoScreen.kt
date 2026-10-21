package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.ui.components.ProductoIcono
import com.yaycate.cafecampus.ui.components.recursoImagen
import com.yaycate.cafecampus.ui.components.slugProducto
import com.yaycate.cafecampus.viewmodel.MenuViewModel
import kotlinx.coroutines.launch

private data class OpcionPagoData(
    val nombre: String,
    val imagenNombre: String,
    val fallbackIcono: ImageVector
)

private val opcionesPago = listOf(
    OpcionPagoData("Pagar en caja", "pago_caja", Icons.Default.CreditCard),
    OpcionPagoData("Yape", "pago_yape", Icons.Default.QrCode),
    OpcionPagoData("Plin", "pago_plin", Icons.Default.PhoneAndroid)
)

private val opcionesHora = listOf("Ahora", "10:30", "11:00", "11:30")

@Composable
fun ConfirmarPedidoScreen(
    vm: MenuViewModel,
    clienteId: Int,
    recibirPedidos: Boolean,
    onConfirmado: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val carrito by vm.carrito.collectAsState()
    val subtotal by vm.subtotalCarrito.collectAsState()
    val recargo by vm.recargoPago.collectAsState()
    val total by vm.totalCarrito.collectAsState()
    val pagoSeleccionado by vm.metodoPago.collectAsState()
    val productos by vm.productos.collectAsState()
    val scope = rememberCoroutineScope()

    var horaRecojo by rememberSaveable { mutableStateOf("") }
    var nota by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(titulo = "Confirmar pedido", onVolver = onVolver) }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(relleno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!recibirPedidos) {
                Text(
                    text = "Cafetería cerrada por ahora. No se pueden hacer pedidos.",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (carrito.isEmpty()) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ir al menú")
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        carrito.forEach { item ->
                            val producto = productos.find { it.id == item.productoId }
                            val icono = producto?.icono ?: "plato"
                            val slug = producto?.nombre?.let { slugProducto(it) } ?: ""
                            val recursoFoto = if (slug.isNotBlank()) recursoImagen(context, "producto_$slug") else 0

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (recursoFoto != 0) {
                                        Image(
                                            painter = painterResource(id = recursoFoto),
                                            contentDescription = item.nombre,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.size(56.dp)
                                        )
                                    } else {
                                        ProductoIcono(
                                            icono = icono,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.nombre,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "S/ %.2f".format(item.precio * item.cantidad),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (item.nota.isNotBlank()) {
                                        Text(
                                            text = "Nota: ${item.nota}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedButton(
                                        onClick = { vm.cambiarCantidad(item.productoId, item.cantidad - 1) }
                                    ) { Text("−") }
                                    Text(
                                        text = item.cantidad.toString(),
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                    OutlinedButton(
                                        onClick = { vm.cambiarCantidad(item.productoId, item.cantidad + 1) }
                                    ) { Text("+") }
                                }
                            }

                            TextButton(onClick = { vm.quitar(item.productoId) }) {
                                Text("Quitar")
                            }
                        }
                    }
                }

                Text(
                    text = "Hora de recojo",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    opcionesHora.forEach { hora ->
                        FilterChip(
                            selected = horaRecojo == hora,
                            onClick = { horaRecojo = hora },
                            label = { Text(hora) }
                        )
                    }
                }

                Text(
                    text = "Forma de pago",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    opcionesPago.forEach { opcion ->
                        val seleccionada = pagoSeleccionado == opcion.nombre
                        val recursoImg = recursoImagen(context, opcion.imagenNombre)

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { vm.cambiarMetodoPago(opcion.nombre) }
                                .then(
                                    if (seleccionada) {
                                        Modifier.border(
                                            width = 2.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    } else Modifier
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (seleccionada) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (recursoImg != 0) {
                                    Image(
                                        painter = painterResource(id = recursoImg),
                                        contentDescription = opcion.nombre,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(36.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = opcion.fallbackIcono,
                                        contentDescription = opcion.nombre,
                                        tint = if (seleccionada) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Text(
                                    text = opcion.nombre,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                CafeTextField(
                    valor = nota,
                    onValorChange = { nota = it },
                    etiqueta = "Nota general (opcional)",
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                        Text("S/ %.2f".format(subtotal), style = MaterialTheme.typography.bodyMedium)
                    }
                    if (recargo > 0.0) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Recargo Yape/Plin", style = MaterialTheme.typography.bodyMedium)
                            Text("S/ %.2f".format(recargo), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "S/ %.2f".format(total),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                CafeButton(
                    texto = "Confirmar pedido",
                    enabled = recibirPedidos && horaRecojo.isNotBlank() && clienteId > 0,
                    onClick = {
                        scope.launch {
                            val id = vm.confirmarPedido(
                                clienteId = clienteId,
                                horaRecojo = horaRecojo,
                                pago = pagoSeleccionado,
                                nota = nota.trim()
                            )
                            onConfirmado(id.toInt())
                        }
                    }
                )

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seguir pidiendo")
                }
            }
        }
    }
}
package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.ui.components.ProductoIcono
import com.yaycate.cafecampus.viewmodel.MenuViewModel

@Composable
fun DetalleProductoScreen(
    vm: MenuViewModel,
    productoId: Int,
    onAgregado: () -> Unit,
    onVolver: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val producto = productos.find { it.id == productoId }

    var cantidad by rememberSaveable { mutableIntStateOf(1) }
    var nota by rememberSaveable { mutableStateOf("") }

    if (producto == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Producto no encontrado", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProductoIcono(icono = producto.icono, modifier = Modifier.size(96.dp))

        Text(
            text = producto.nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = producto.descripcion,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "S/ %.2f".format(producto.precio),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        if (!producto.disponible) {
            Text(
                text = "Agotado",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { if (cantidad > 1) cantidad-- }) { Text("−") }
            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            OutlinedButton(onClick = { cantidad++ }) { Text("+") }
        }

        CafeTextField(
            valor = nota,
            onValorChange = { nota = it },
            etiqueta = "Nota (opcional)",
            modifier = Modifier.fillMaxWidth()
        )

        CafeButton(
            texto = "Agregar al carrito",
            enabled = producto.disponible,
            onClick = {
                vm.agregar(producto, cantidad, nota)
                onAgregado()
            }
        )

        TextButton(onClick = onVolver) { Text("Volver") }
    }
}
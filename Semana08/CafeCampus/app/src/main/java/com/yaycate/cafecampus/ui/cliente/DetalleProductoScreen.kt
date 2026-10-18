package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.ui.components.AppTopBar
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

    Scaffold(
        topBar = { AppTopBar(titulo = "Detalle del producto", onVolver = onVolver) }
    ) { relleno ->
        if (producto == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentAlignment = Alignment.Center
            ) {
                Text("Producto no encontrado", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            DetalleContenido(
                producto = producto,
                relleno = relleno,
                onAgregar = { cantidad, nota ->
                    vm.agregar(producto, cantidad, nota)
                    onAgregado()
                }
            )
        }
    }
}

@Composable
private fun DetalleContenido(
    producto: Producto,
    relleno: PaddingValues,
    onAgregar: (Int, String) -> Unit
) {
    var cantidad by rememberSaveable { mutableIntStateOf(1) }
    var nota by rememberSaveable { mutableStateOf("") }
    val total = producto.precio * cantidad

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(relleno)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            ProductoIcono(
                icono = producto.icono,
                modifier = Modifier.size(96.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "S/ %.2f".format(producto.precio),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Surface(
            shape = RoundedCornerShape(50),
            color = if (producto.disponible) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        ) {
            Text(
                text = if (producto.disponible) "Disponible" else "Agotado",
                style = MaterialTheme.typography.labelLarge,
                color = if (producto.disponible) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        Text(
            text = producto.descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cantidad",
                style = MaterialTheme.typography.titleMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { if (cantidad > 1) cantidad-- },
                    enabled = producto.disponible
                ) { Text("−") }
                Text(
                    text = cantidad.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                OutlinedButton(
                    onClick = { cantidad++ },
                    enabled = producto.disponible
                ) { Text("+") }
            }
        }

        CafeTextField(
            valor = nota,
            onValorChange = { nota = it },
            etiqueta = "Nota para la cocina (opcional)",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        CafeButton(
            texto = if (producto.disponible) {
                "Agregar al pedido · S/ %.2f".format(total)
            } else {
                "Producto agotado"
            },
            enabled = producto.disponible,
            onClick = { onAgregar(cantidad, nota.trim()) }
        )
    }
}
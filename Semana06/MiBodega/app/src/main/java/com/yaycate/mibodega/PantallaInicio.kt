package com.yaycate.mibodega

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaInicio(
    onProductoClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mi Bodega", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productosEjemplo, key = { it.id }) { producto ->
                ProductoItem(producto, onClick = { onProductoClick(producto.id) })
            }
        }
    }
}

@Composable
private fun ProductoItem(producto: Producto, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(producto.categoria, style = MaterialTheme.typography.bodySmall)
            }
            Text("S/ %.2f".format(producto.precio), style = MaterialTheme.typography.titleMedium)
        }
    }
}
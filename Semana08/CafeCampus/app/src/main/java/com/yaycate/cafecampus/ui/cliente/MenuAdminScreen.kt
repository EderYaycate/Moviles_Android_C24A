package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.ProductoIcono
import com.yaycate.cafecampus.viewmodel.MenuAdminViewModel
import androidx.compose.foundation.rememberScrollState as rememberScrollStateAlias

private const val TODO_CATEGORIAS = "Todo"

@Composable
fun MenuAdminScreen(
    vm: MenuAdminViewModel,
    onEditar: (Int) -> Unit,
    onNuevo: () -> Unit,
    onVolver: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    var aEliminar by remember { mutableStateOf<Producto?>(null) }
    var categoriaElegida by rememberSaveable { mutableStateOf(TODO_CATEGORIAS) }

    val categorias = listOf(TODO_CATEGORIAS) + productos.map { it.categoria }.distinct().sorted()
    val seleccion = if (categoriaElegida in categorias) categoriaElegida else TODO_CATEGORIAS
    val mostrados = productos.filter {
        seleccion == TODO_CATEGORIAS || it.categoria == seleccion
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Gestión del menú", onVolver = onVolver) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevo) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo producto"
                )
            }
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categorias.forEach { cat ->
                    FilterChip(
                        selected = seleccion == cat,
                        onClick = { categoriaElegida = cat },
                        label = { Text(cat) }
                    )
                }
            }

            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos. Crea el primero con el botón +",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else if (mostrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos en esta categoría",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(mostrados, key = { it.id }) { producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ProductoIcono(
                                        icono = producto.icono,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = producto.nombre,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "${producto.categoria} · S/ %.2f".format(producto.precio),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (producto.disponible) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.errorContainer
                                        },
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = if (producto.disponible) "Disponible" else "Agotado",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (producto.disponible) {
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            } else {
                                                MaterialTheme.colorScheme.onErrorContainer
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Switch(
                                    checked = producto.disponible,
                                    onCheckedChange = { vm.cambiarDisponibilidad(producto.id, it) }
                                )
                                IconButton(onClick = { onEditar(producto.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar"
                                    )
                                }
                                IconButton(onClick = { aEliminar = producto }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    aEliminar?.let { producto ->
        AlertDialog(
            onDismissRequest = { aEliminar = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que quieres eliminar \"${producto.nombre}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminar(producto)
                    aEliminar = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { aEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.ProductoCard
import com.yaycate.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProductoClick: (Int) -> Unit,
    onVerCarrito: () -> Unit,
    onVerPedidos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()
    val cantidadItems = carrito.sumOf { it.cantidad }

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Menú",
                acciones = {
                    TextButton(onClick = onCerrarSesion) { Text("Salir") }
                    IconButton(onClick = onVerPedidos) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Mis pedidos"
                        )
                    }
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadItems > 0) {
                                    Badge { Text(cantidadItems.toString()) }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                }
            )
        }
    ) { relleno ->
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos disponibles",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos, key = { it.id }) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto.id) }
                    )
                }
            }
        }
    }
}
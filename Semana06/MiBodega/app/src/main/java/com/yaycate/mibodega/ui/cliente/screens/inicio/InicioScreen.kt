package com.yaycate.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.mibodega.ui.cliente.FondoTarjeta
import com.yaycate.mibodega.ui.cliente.TextoOscuro
import com.yaycate.mibodega.ui.cliente.VerdeBodega
import com.yaycate.mibodega.ui.cliente.formatoSoles
import com.yaycate.mibodega.ui.cliente.modelo.DatosFake
import com.yaycate.mibodega.ui.cliente.modelo.Producto

private val categorias = listOf(
    "Todos" to "🛒",
    "Bebidas" to "🥤",
    "Abarrotes" to "🛍️",
    "Snacks" to "🍪"
)

@Composable
fun InicioScreen(
    cantidadCarrito: Int = 0,
    onProductoClick: (Producto) -> Unit = {},
    onAgregarAlCarrito: (Producto) -> Unit = {},
    onIrAlCarrito: () -> Unit = {}
) {
    var textoBusqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val productosFiltrados = DatosFake.productos.filter { producto ->
        val coincideCategoria =
            categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda =
            producto.nombre.contains(textoBusqueda.trim(), ignoreCase = true)
        coincideCategoria && coincideBusqueda
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextoOscuro)) { append("Mi ") }
                    withStyle(SpanStyle(color = VerdeBodega)) { append("Bodega") }
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onIrAlCarrito) {
                BadgedBox(
                    badge = {
                        if (cantidadCarrito > 0) {
                            Badge { Text(cantidadCarrito.toString()) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito de compras",
                        tint = TextoOscuro
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            placeholder = { Text("Buscar productos...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categorias) { (nombre, emoji) ->
                FilterChip(
                    selected = nombre == categoriaSeleccionada,
                    onClick = { categoriaSeleccionada = nombre },
                    label = { Text("$emoji $nombre") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeBodega,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Productos destacados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (productosFiltrados.isEmpty()) {
            Text(
                text = "No se encontraron productos",
                fontSize = 14.sp,
                color = Color.Gray
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados, key = { it.id }) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onProductoClick = { onProductoClick(producto) },
                        onAgregarAlCarrito = { onAgregarAlCarrito(producto) }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaProducto(
    producto: Producto,
    onProductoClick: () -> Unit,
    onAgregarAlCarrito: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductoClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                producto.imagenRes?.let { imagen ->
                    Image(
                        painter = painterResource(id = imagen),
                        contentDescription = producto.nombre,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextoOscuro
            )
            Text(
                text = producto.presentacion,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatoSoles(producto.precio),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = VerdeBodega
                )
                IconButton(
                    onClick = onAgregarAlCarrito,
                    modifier = Modifier
                        .size(36.dp)
                        .background(VerdeBodega, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar al carrito",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
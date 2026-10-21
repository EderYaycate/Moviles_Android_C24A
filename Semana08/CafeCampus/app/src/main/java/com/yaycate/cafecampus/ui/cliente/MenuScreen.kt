package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.ui.components.ProductoIcono
import com.yaycate.cafecampus.ui.components.recursoImagen
import com.yaycate.cafecampus.ui.components.slugProducto
import com.yaycate.cafecampus.viewmodel.MenuViewModel
import com.yaycate.cafecampus.viewmodel.OrdenMenu

private const val TODAS = "Todo"

@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProductoClick: (Int) -> Unit,
    onVerCarrito: () -> Unit
) {
    val context = LocalContext.current
    val productos by vm.productos.collectAsState()
    val productosFiltrados by vm.productosFiltrados.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val ordenActual by vm.orden.collectAsState()
    val favoritos by vm.favoritos.collectAsState()
    val carrito by vm.carrito.collectAsState()
    val cantidadTotal = carrito.sumOf { it.cantidad }

    var categoriaElegida by rememberSaveable { mutableStateOf(TODAS) }

    val categorias = listOf(TODAS) + productos.map { it.categoria }.distinct().sorted()
    val seleccion = if (categoriaElegida in categorias) categoriaElegida else TODAS
    val mostrados = productosFiltrados.filter { p ->
        seleccion == TODAS || p.categoria == seleccion
    }

    val productosFav = productos.filter { it.id in favoritos }

    Scaffold { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "¿Qué se te antoja hoy?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onVerCarrito) {
                    BadgedBox(
                        badge = {
                            if (cantidadTotal > 0) {
                                Badge { Text(cantidadTotal.toString()) }
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

            OutlinedTextField(
                value = busqueda,
                onValueChange = { vm.cambiarBusqueda(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar en el menú...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(50)
            )

            // Sección de Favoritos si existe al menos uno
            if (productosFav.isNotEmpty()) {
                Text(
                    text = "Favoritos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    productosFav.forEach { prod ->
                        TarjetaFavoritoPequena(
                            producto = prod,
                            onClick = { onProductoClick(prod.id) },
                            onToggleFavorito = { vm.toggleFavorito(prod.id) }
                        )
                    }
                }
            }

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
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

            Text(
                text = "Ordenar por",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = ordenActual == OrdenMenu.RECOMENDADOS,
                    onClick = { vm.cambiarOrden(OrdenMenu.RECOMENDADOS) },
                    label = { Text("Recomendados") }
                )
                FilterChip(
                    selected = ordenActual == OrdenMenu.PRECIO_MENOR_MAYOR,
                    onClick = { vm.cambiarOrden(OrdenMenu.PRECIO_MENOR_MAYOR) },
                    label = { Text("Precio: menor a mayor") }
                )
                FilterChip(
                    selected = ordenActual == OrdenMenu.PRECIO_MAYOR_MENOR,
                    onClick = { vm.cambiarOrden(OrdenMenu.PRECIO_MAYOR_MENOR) },
                    label = { Text("Precio: mayor a menor") }
                )
            }

            Text(
                text = "Productos",
                style = MaterialTheme.typography.titleMedium
            )

            when {
                productos.isEmpty() -> Text(
                    text = "No hay productos disponibles por ahora",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                mostrados.isEmpty() -> Text(
                    text = "No encontramos productos con esa búsqueda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                else -> mostrados.chunked(2).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        fila.forEach { producto ->
                            TarjetaProducto(
                                producto = producto,
                                esFavorito = producto.id in favoritos,
                                onClick = { onProductoClick(producto.id) },
                                onAgregar = { vm.agregar(producto, 1) },
                                onToggleFavorito = { vm.toggleFavorito(producto.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (fila.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaFavoritoPequena(
    producto: Producto,
    onClick: () -> Unit,
    onToggleFavorito: () -> Unit
) {
    val context = LocalContext.current
    val slug = slugProducto(producto.nombre)
    val recursoFoto = recursoImagen(context, "producto_$slug")

    Card(
        modifier = Modifier
            .width(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (recursoFoto != 0) {
                    Image(
                        painter = painterResource(id = recursoFoto),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ProductoIcono(icono = producto.icono, modifier = Modifier.size(36.dp))
                }

                Surface(
                    onClick = onToggleFavorito,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier
                            .padding(4.dp)
                            .size(16.dp)
                    )
                }
            }

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = "S/ %.2f".format(producto.precio),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TarjetaProducto(
    producto: Producto,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onAgregar: () -> Unit,
    onToggleFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val slug = slugProducto(producto.nombre)
    val recursoFoto = recursoImagen(context, "producto_$slug")

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (recursoFoto != 0) {
                    Image(
                        painter = painterResource(id = recursoFoto),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ProductoIcono(
                        icono = producto.icono,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Surface(
                    onClick = onToggleFavorito,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (esFavorito) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(18.dp)
                    )
                }
            }

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
maxLines = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "S/ %.2f".format(producto.precio),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (!producto.disponible) {
                        Text(
                            text = "Agotado",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                IconButton(
                    onClick = onAgregar,
                    enabled = producto.disponible,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = if (producto.disponible) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
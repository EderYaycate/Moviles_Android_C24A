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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.ui.components.ProductoIcono
import com.yaycate.cafecampus.viewmodel.MenuAdminViewModel

private val categoriasSugeridas = listOf("Bebidas", "Comidas", "Panadería", "Desayunos", "Almuerzos", "Snacks")

private val iconosDisponibles = listOf(
    "cafe" to "Café",
    "plato" to "Plato",
    "jugo" to "Jugo",
    "pan" to "Pan",
    "botella" to "Botella"
)

@Composable
fun FormularioProductoScreen(
    vm: MenuAdminViewModel,
    productoId: Int,
    onGuardado: () -> Unit,
    onVolver: () -> Unit
) {
    val productos by vm.productos.collectAsState()
    val esNuevo = productoId <= 0
    val existente = productos.find { it.id == productoId }

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = if (esNuevo) "Nuevo producto" else "Editar producto",
                onVolver = onVolver
            )
        }
    ) { relleno ->
        if (!esNuevo && existente == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando producto...", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            key(productoId) {
                FormularioContenido(
                    existente = existente,
                    relleno = relleno,
                    onGuardar = { producto ->
                        vm.guardar(producto)
                        onGuardado()
                    },
                    onCancelar = onVolver
                )
            }
        }
    }
}

@Composable
private fun FormularioContenido(
    existente: Producto?,
    relleno: PaddingValues,
    onGuardar: (Producto) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf(existente?.nombre ?: "") }
    var descripcion by rememberSaveable { mutableStateOf(existente?.descripcion ?: "") }
    var categoria by rememberSaveable { mutableStateOf(existente?.categoria ?: "") }
    var precioTexto by rememberSaveable {
        mutableStateOf(existente?.precio?.toString() ?: "")
    }
    var icono by rememberSaveable { mutableStateOf(existente?.icono ?: "plato") }
    var disponible by rememberSaveable { mutableStateOf(existente?.disponible ?: true) }

    val precio = precioTexto.replace(',', '.').toDoubleOrNull()
    val errorPrecio = if (precioTexto.isNotBlank() && precio == null) "Precio no válido" else null
    val valido = nombre.isNotBlank() && precio != null && precio >= 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(relleno)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Vista previa del icono
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ProductoIcono(
                        icono = icono,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Column {
                    Text(
                        text = "Icono del producto",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Elige uno de los iconos de abajo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        CafeTextField(
            valor = nombre,
            onValorChange = { nombre = it },
            etiqueta = "Nombre del producto",
            modifier = Modifier.fillMaxWidth()
        )

        CafeTextField(
            valor = descripcion,
            onValorChange = { descripcion = it },
            etiqueta = "Descripción",
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Categoría",
            style = MaterialTheme.typography.titleSmall
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriasSugeridas.forEach { cat ->
                FilterChip(
                    selected = categoria == cat,
                    onClick = { categoria = cat },
                    label = { Text(cat) }
                )
            }
        }
        CafeTextField(
            valor = categoria,
            onValorChange = { categoria = it },
            etiqueta = "O escribe otra categoría",
            modifier = Modifier.fillMaxWidth()
        )

        CafeTextField(
            valor = precioTexto,
            onValorChange = { precioTexto = it },
            etiqueta = "Precio (S/)",
            modifier = Modifier.fillMaxWidth(),
            error = errorPrecio
        )

        Text(
            text = "Icono",
            style = MaterialTheme.typography.titleSmall
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            iconosDisponibles.forEach { (clave, etiqueta) ->
                FilterChip(
                    selected = icono == clave,
                    onClick = { icono = clave },
                    label = { Text(etiqueta) }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Disponible en el menú",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Los estudiantes podrán pedirlo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = disponible,
                onCheckedChange = { disponible = it }
            )
        }

        CafeButton(
            texto = "Guardar producto",
            enabled = valido,
            onClick = {
                onGuardar(
                    Producto(
                        id = existente?.id ?: 0,
                        nombre = nombre.trim(),
                        descripcion = descripcion.trim(),
                        categoria = categoria.trim(),
                        precio = precio ?: 0.0,
                        disponible = disponible,
                        icono = icono
                    )
                )
            }
        )

        OutlinedButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}
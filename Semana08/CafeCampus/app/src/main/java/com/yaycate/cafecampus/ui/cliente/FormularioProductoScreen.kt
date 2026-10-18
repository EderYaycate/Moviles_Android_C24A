package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.local.entity.Producto
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.viewmodel.MenuAdminViewModel

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
                    }
                )
            }
        }
    }
}

@Composable
private fun FormularioContenido(
    existente: Producto?,
    relleno: PaddingValues,
    onGuardar: (Producto) -> Unit
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CafeTextField(
            valor = nombre,
            onValorChange = { nombre = it },
            etiqueta = "Nombre",
            modifier = Modifier.fillMaxWidth()
        )
        CafeTextField(
            valor = descripcion,
            onValorChange = { descripcion = it },
            etiqueta = "Descripción",
            modifier = Modifier.fillMaxWidth()
        )
        CafeTextField(
            valor = categoria,
            onValorChange = { categoria = it },
            etiqueta = "Categoría (ej. cafe, plato, jugo)",
            modifier = Modifier.fillMaxWidth()
        )
        CafeTextField(
            valor = precioTexto,
            onValorChange = { precioTexto = it },
            etiqueta = "Precio (S/)",
            modifier = Modifier.fillMaxWidth(),
            error = errorPrecio
        )
        CafeTextField(
            valor = icono,
            onValorChange = { icono = it },
            etiqueta = "Icono (cafe, plato, jugo, pan, botella)",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Disponible",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = disponible,
                onCheckedChange = { disponible = it }
            )
        }

        CafeButton(
            texto = "Guardar",
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
                        icono = icono.trim()
                    )
                )
            }
        )
    }
}
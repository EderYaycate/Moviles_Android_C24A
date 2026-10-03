package com.yaycate.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.mibodega.ui.cliente.BotonPrincipal
import com.yaycate.mibodega.ui.cliente.CampoBodega
import com.yaycate.mibodega.ui.cliente.FondoTarjeta
import com.yaycate.mibodega.ui.cliente.TextoOscuro
import com.yaycate.mibodega.ui.cliente.VerdeBodega
import com.yaycate.mibodega.ui.cliente.VerdeClaro

import com.yaycate.mibodega.ui.cliente.modelo.DatosUsuario

@Composable
fun PerfilScreen(
    usuario: DatosUsuario,
    cantidadCarrito: Int,
    onGuardar: (DatosUsuario) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var editando by remember { mutableStateOf(false) }
    var nombre by remember(usuario) { mutableStateOf(usuario.nombre) }
    var telefono by remember(usuario) { mutableStateOf(usuario.telefono) }
    var direccion by remember(usuario) { mutableStateOf(usuario.direccion) }
    var referencia by remember(usuario) { mutableStateOf(usuario.referencia) }

    val nombreMostrado = usuario.nombre.ifBlank { "Cliente Mi Bodega" }
    val iniciales = nombreMostrado
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first() }
        .joinToString("")
        .uppercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Column(

            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(VerdeClaro),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeBodega
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = nombreMostrado,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
            if (usuario.telefono.isNotBlank()) {
                Text(text = usuario.telefono, fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(

            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis datos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
            if (!editando) {
                TextButton(onClick = { editando = true }) {
                    Text("Editar", color = VerdeBodega, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!editando) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoTarjeta, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FilaDato(Icons.Default.Person, "Nombre", usuario.nombre)
                FilaDato(Icons.Default.Phone, "Teléfono", usuario.telefono)
                FilaDato(Icons.Default.LocationOn, "Dirección", usuario.direccion)
                FilaDato(Icons.Default.Info, "Referencia", usuario.referencia)
            }

        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoBodega(nombre, { nombre = it }, "Nombre completo")
                CampoBodega(telefono, { telefono = it }, "Teléfono", KeyboardType.Phone)
                CampoBodega(direccion, { direccion = it }, "Dirección de entrega")
                CampoBodega(referencia, { referencia = it }, "Referencia")
            }
            Spacer(modifier = Modifier.height(16.dp))
            BotonPrincipal(
                texto = "Guardar cambios",
                onClick = {
                    onGuardar(DatosUsuario(nombre, telefono, direccion, referencia))
                    editando = false
                },
                enabled = nombre.isNotBlank()
            )
            TextButton(
                onClick = {
                    nombre = usuario.nombre
                    telefono = usuario.telefono
                    direccion = usuario.direccion
                    referencia = usuario.referencia
                    editando = false
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cancelar", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        Text(
            text = "Mi actividad",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FondoTarjeta, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = VerdeBodega)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Productos en tu carrito",
                modifier = Modifier.weight(1f),
                color = TextoOscuro
            )
            Text(
                text = cantidadCarrito.toString(),
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = onCerrarSesion,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = Color(0xFFC62828)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar sesión", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FilaDato(icono: ImageVector, etiqueta: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = VerdeBodega)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = etiqueta, fontSize = 12.sp, color = Color.Gray)
            Text(
                text = valor.ifBlank { "Sin registrar" },
                fontSize = 15.sp,
                color = TextoOscuro
            )
        }
    }
}
package com.yaycate.mibodega.ui.cliente.screens.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaycate.mibodega.ui.cliente.BotonPrincipal
import com.yaycate.mibodega.ui.cliente.CampoBodega
import com.yaycate.mibodega.ui.cliente.TextoOscuro
import com.yaycate.mibodega.ui.cliente.VerdeBodega
import com.yaycate.mibodega.ui.cliente.VerdeClaro

@Composable
fun RegistroScreen(
    onCrearCuenta: (String, String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Crear cuenta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }
        Text(
            text = "Complete los datos para continuar",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(VerdeClaro)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = VerdeBodega,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CampoBodega(nombre, { nombre = it }, "Nombre completo")
            CampoBodega(telefono, { telefono = it }, "Teléfono", KeyboardType.Phone)
            CampoBodega(direccion, { direccion = it }, "Dirección de entrega")
            CampoBodega(referencia, { referencia = it }, "Referencia")
        }

        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Crear cuenta",
            onClick = { onCrearCuenta(nombre, telefono, direccion, referencia) },
            enabled = nombre.isNotBlank() && telefono.isNotBlank()
        )
    }
}
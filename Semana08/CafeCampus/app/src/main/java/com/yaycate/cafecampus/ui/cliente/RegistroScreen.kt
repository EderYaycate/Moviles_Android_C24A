package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.components.AppTopBar
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.ui.theme.CafeTheme
import com.yaycate.cafecampus.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    vm: RegistroViewModel,
    onCuentaCreada: () -> Unit,
    onVolver: () -> Unit
) {
    val error by vm.error.collectAsState()
    val exito by vm.exito.collectAsState()
    val cargando by vm.cargando.collectAsState()

    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmarClave by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(exito) {
        if (exito) {
            vm.limpiarEstado()
            onCuentaCreada()
        }
    }

    CafeTheme(rol = Rol.CLIENTE) {
        Scaffold(
            topBar = { AppTopBar(titulo = "Registro de Estudiante", onVolver = onVolver) }
        ) { relleno ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(relleno)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Crea tu cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ingresa tus datos para registrarte como estudiante",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(8.dp))

                CafeTextField(
                    valor = nombre,
                    onValorChange = { nombre = it },
                    etiqueta = "Nombre completo",
                    modifier = Modifier.fillMaxWidth()
                )

                CafeTextField(
                    valor = correo,
                    onValorChange = { correo = it },
                    etiqueta = "Correo electrónico",
                    modifier = Modifier.fillMaxWidth()
                )

                CafeTextField(
                    valor = clave,
                    onValorChange = { clave = it },
                    etiqueta = "Contraseña",
                    esClave = true,
                    modifier = Modifier.fillMaxWidth()
                )

                CafeTextField(
                    valor = confirmarClave,
                    onValorChange = { confirmarClave = it },
                    etiqueta = "Confirmar contraseña",
                    esClave = true,
                    modifier = Modifier.fillMaxWidth()
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(8.dp))

                CafeButton(
                    texto = if (cargando) "Creando cuenta..." else "Crear cuenta",
                    enabled = !cargando,
                    onClick = {
                        vm.registrar(
                            nombre = nombre,
                            correo = correo,
                            clave = clave,
                            confirmarClave = confirmarClave
                        )
                    }
                )

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al inicio de sesión")
                }
            }
        }
    }
}
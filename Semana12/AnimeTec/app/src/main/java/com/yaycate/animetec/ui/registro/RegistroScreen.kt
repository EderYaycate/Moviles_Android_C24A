package com.yaycate.animetec.ui.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.R
import com.yaycate.animetec.domain.model.Sesion
import com.yaycate.animetec.ui.UiState

@Composable
fun RegistroRoute(
    viewModel: RegistroViewModel,
    onSesionIniciada: (Sesion) -> Unit,
    onVolverLogin: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmarClave by rememberSaveable { mutableStateOf("") }

    val e = estado
    val sesion = if (e is UiState.Exito) e.datos else null
    LaunchedEffect(sesion) { sesion?.let(onSesionIniciada) }

    RegistroScreen(
        correo = correo,
        clave = clave,
        confirmarClave = confirmarClave,
        cargando = e is UiState.Cargando,
        error = (e as? UiState.Error)?.mensaje,
        onCorreoChange = { correo = it; viewModel.limpiarError() },
        onClaveChange = { clave = it; viewModel.limpiarError() },
        onConfirmarClaveChange = { confirmarClave = it; viewModel.limpiarError() },
        onCrearCuenta = { viewModel.registrar(correo, clave, confirmarClave) },
        onVolverLogin = onVolverLogin
    )
}

@Composable
fun RegistroScreen(
    correo: String,
    clave: String,
    confirmarClave: String,
    cargando: Boolean,
    error: String?,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onConfirmarClaveChange: (String) -> Unit,
    onCrearCuenta: () -> Unit,
    onVolverLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo_animetec),
            contentDescription = "Logo de AnimeTec",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
        )
        Spacer(Modifier.height(16.dp))
        Text("Crear cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo") },
            singleLine = true,
            enabled = !cargando,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = clave,
            onValueChange = onClaveChange,
            label = { Text("Contraseña") },
            singleLine = true,
            enabled = !cargando,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmarClave,
            onValueChange = onConfirmarClaveChange,
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            enabled = !cargando,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Spacer(Modifier.height(12.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onCrearCuenta,
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (cargando) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Crear cuenta")
            }
        }

        Spacer(Modifier.height(8.dp))
        TextButton(
            onClick = onVolverLogin,
            enabled = !cargando
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

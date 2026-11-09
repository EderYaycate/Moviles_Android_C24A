package com.yaycate.animetec.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.R
import com.yaycate.animetec.domain.model.Sesion
import com.yaycate.animetec.ui.UiState
import com.yaycate.animetec.ui.components.TituloAnimeTec

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onSesionIniciada: (Sesion) -> Unit,
    onIrARegistro: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val resultadoRecuperacion by viewModel.resultadoRecuperacion.collectAsStateWithLifecycle()
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    val e = estado
    val sesion = if (e is UiState.Exito) e.datos else null
    LaunchedEffect(sesion) { sesion?.let(onSesionIniciada) }

    LoginScreen(
        correo = correo,
        clave = clave,
        cargando = e is UiState.Cargando,
        error = (e as? UiState.Error)?.mensaje,
        resultadoRecuperacion = resultadoRecuperacion,
        onCorreoChange = { correo = it; viewModel.limpiarError() },
        onClaveChange = { clave = it; viewModel.limpiarError() },
        onIngresar = { viewModel.iniciarSesion(correo, clave) },
        onRecuperarClave = { email -> viewModel.recuperarClave(email) },
        onLimpiarRecuperacion = { viewModel.limpiarRecuperacion() },
        onIrARegistro = onIrARegistro
    )
}

@Composable
fun LoginScreen(
    correo: String,
    clave: String,
    cargando: Boolean,
    error: String?,
    resultadoRecuperacion: UiState<String>?,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onIngresar: () -> Unit,
    onRecuperarClave: (String) -> Unit,
    onLimpiarRecuperacion: () -> Unit,
    onIrARegistro: () -> Unit = {}
) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    var correoRecuperacion by remember(correo, mostrarDialogo) { mutableStateOf(correo) }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                onLimpiarRecuperacion()
            },
            title = { Text("Recuperar contraseña") },
            text = {
                Column {
                    Text("Ingresa tu correo electrónico para restablecer tu contraseña:")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = correoRecuperacion,
                        onValueChange = {
                            correoRecuperacion = it
                            onLimpiarRecuperacion()
                        },
                        label = { Text("Correo") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (resultadoRecuperacion != null) {
                        Spacer(Modifier.height(8.dp))
                        when (resultadoRecuperacion) {
                            is UiState.Exito -> {
                                Text(
                                    text = resultadoRecuperacion.datos,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            is UiState.Error -> {
                                Text(
                                    text = resultadoRecuperacion.mensaje,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            is UiState.Cargando -> {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRecuperarClave(correoRecuperacion)
                    },
                    enabled = resultadoRecuperacion !is UiState.Cargando
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        onLimpiarRecuperacion()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500), initialOffsetY = { it / 4 })
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
                        .size(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
                Spacer(Modifier.height(16.dp))
                TituloAnimeTec(style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = onCorreoChange,
                    label = { Text("Correo") },
                    singleLine = true,
                    enabled = !cargando,
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
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
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(20.dp))

                val primary = MaterialTheme.colorScheme.primary
                val secondary = MaterialTheme.colorScheme.secondary
                val buttonBrush = Brush.horizontalGradient(
                    colors = listOf(primary.copy(alpha = 0.9f), secondary.copy(alpha = 0.9f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(buttonBrush)
                        .clickable(enabled = !cargando, onClick = onIngresar),
                    contentAlignment = Alignment.Center
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Ingresar",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        correoRecuperacion = correo
                        onLimpiarRecuperacion()
                        mostrarDialogo = true
                    },
                    enabled = !cargando
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick = onIrARegistro,
                    enabled = !cargando
                ) {
                    val regColor = MaterialTheme.colorScheme.primary
                    val textColor = MaterialTheme.colorScheme.onSurface
                    val annotated = buildAnnotatedString {
                        withStyle(SpanStyle(color = textColor)) {
                            append("¿Eres nuevo? ")
                        }
                        withStyle(SpanStyle(color = regColor, fontWeight = FontWeight.Bold)) {
                            append("Regístrate")
                        }
                    }
                    Text(text = annotated)
                }
            }
        }
    }
}

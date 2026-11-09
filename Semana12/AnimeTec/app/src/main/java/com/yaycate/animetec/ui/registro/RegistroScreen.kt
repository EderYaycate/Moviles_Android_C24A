package com.yaycate.animetec.ui.registro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
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
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

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
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
                Spacer(Modifier.height(16.dp))

                val primary = MaterialTheme.colorScheme.primary
                val secondary = MaterialTheme.colorScheme.secondary
                val tituloTexto = buildAnnotatedString {
                    withStyle(SpanStyle(color = primary, fontWeight = FontWeight.Bold)) {
                        append("Crear ")
                    }
                    withStyle(SpanStyle(color = secondary, fontWeight = FontWeight.Bold)) {
                        append("cuenta")
                    }
                }
                Text(text = tituloTexto, style = MaterialTheme.typography.headlineMedium)
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
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Ocultar" else "Ver")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(if (confirmPasswordVisible) "Ocultar" else "Ver")
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedVisibility(
                    visible = error != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    if (error != null) {
                        Spacer(Modifier.height(12.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                val buttonBrush = Brush.horizontalGradient(
                    colors = listOf(primary.copy(alpha = 0.9f), secondary.copy(alpha = 0.9f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(buttonBrush)
                        .clickable(enabled = !cargando, onClick = onCrearCuenta),
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
                            text = "Crear cuenta",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = onVolverLogin,
                    enabled = !cargando
                ) {
                    val loginColor = MaterialTheme.colorScheme.primary
                    val textColor = MaterialTheme.colorScheme.onSurface
                    val annotated = buildAnnotatedString {
                        withStyle(SpanStyle(color = textColor)) {
                            append("¿Ya tienes cuenta? ")
                        }
                        withStyle(SpanStyle(color = loginColor, fontWeight = FontWeight.Bold)) {
                            append("Inicia sesión")
                        }
                    }
                    Text(text = annotated)
                }
            }
        }
    }
}

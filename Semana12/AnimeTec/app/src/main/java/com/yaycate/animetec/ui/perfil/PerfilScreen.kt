package com.yaycate.animetec.ui.perfil

import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yaycate.animetec.domain.model.Perfil
import com.yaycate.animetec.ui.UiState
import com.yaycate.animetec.ui.components.Cargando
import com.yaycate.animetec.ui.components.ErrorView
import com.yaycate.animetec.ui.theme.ModoTema
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilRoute(
    viewModel: PerfilViewModel,
    modifier: Modifier = Modifier,
    modoTema: ModoTema = ModoTema.SISTEMA,
    onCambiarModo: (ModoTema) -> Unit = {},
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    val guardando by viewModel.guardando.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mensaje) {
        mensaje?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.limpiarMensaje()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { padding ->
        Crossfade(targetState = estado, modifier = Modifier.padding(padding), label = "perfilStateCrossfade") { state ->
            when (val e = state) {
                is UiState.Cargando -> Cargando(Modifier.fillMaxSize())
                is UiState.Error -> ErrorView(
                    mensaje = e.mensaje,
                    onReintentar = viewModel::cargar,
                    modifier = Modifier.fillMaxSize()
                )
                is UiState.Exito -> PerfilContenido(
                    perfil = e.datos,
                    modoTema = modoTema,
                    onCambiarModo = onCambiarModo,
                    guardando = guardando,
                    onGuardarNombre = viewModel::guardarNombre,
                    onCambiarFoto = viewModel::cambiarFoto,
                    onCerrarSesion = {
                        viewModel.cerrarSesion()
                        onCerrarSesion()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PerfilContenido(
    perfil: Perfil,
    modoTema: ModoTema,
    onCambiarModo: (ModoTema) -> Unit,
    guardando: Boolean,
    onGuardarNombre: (String) -> Unit,
    onCambiarFoto: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var nombreInput by remember(perfil.nombre) { mutableStateOf(perfil.nombre) }
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onCambiarFoto(it.toString()) }
    }

    if (mostrarDialogoCerrarSesion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCerrarSesion = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoCerrarSesion = false
                        onCerrarSesion()
                    }
                ) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCerrarSesion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = perfil.fotoBase64, label = "avatarCrossfade") { fotoB64 ->
                if (!fotoB64.isNullOrBlank()) {
                    val byteArray = remember(fotoB64) {
                        try {
                            Base64.decode(fotoB64, Base64.NO_WRAP)
                        } catch (e: Exception) {
                            ByteArray(0)
                        }
                    }
                    AsyncImage(
                        model = byteArray,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        val iniciales = perfil.nombre.split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString("")
                            .uppercase()
                        if (iniciales.isNotBlank()) {
                            Text(
                                text = iniciales,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            enabled = !guardando
        ) {
            Text("Cambiar foto")
        }

        OutlinedTextField(
            value = nombreInput,
            onValueChange = { nombreInput = it },
            label = { Text("Nombre") },
            singleLine = true,
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onGuardarNombre(nombreInput) },
            enabled = !guardando && nombreInput.trim() != perfil.nombre && nombreInput.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        AnimatedVisibility(
            visible = guardando,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Guardando...", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AssistChip(
                onClick = {},
                label = { Text(perfil.correo) },
                modifier = Modifier.weight(1f)
            )
            AssistChip(
                onClick = {},
                label = { Text("Rol: ${perfil.rol.name}") }
            )
        }

        Spacer(Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Apariencia", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = modoTema == ModoTema.SISTEMA,
                    onClick = { onCambiarModo(ModoTema.SISTEMA) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) {
                    Text("Sistema")
                }
                SegmentedButton(
                    selected = modoTema == ModoTema.CLARO,
                    onClick = { onCambiarModo(ModoTema.CLARO) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) {
                    Text("Claro")
                }
                SegmentedButton(
                    selected = modoTema == ModoTema.OSCURO,
                    onClick = { onCambiarModo(ModoTema.OSCURO) },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) {
                    Text("Oscuro")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { mostrarDialogoCerrarSesion = true },
            enabled = !guardando,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text("Cerrar sesión")
        }
    }
}

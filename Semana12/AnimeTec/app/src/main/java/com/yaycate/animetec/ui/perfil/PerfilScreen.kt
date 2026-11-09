package com.yaycate.animetec.ui.perfil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yaycate.animetec.domain.model.Perfil
import com.yaycate.animetec.domain.model.Rol
import com.yaycate.animetec.ui.UiState
import com.yaycate.animetec.ui.components.Cargando
import com.yaycate.animetec.ui.components.ErrorView
import com.yaycate.animetec.ui.theme.ModoTema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
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

    val bitmap by produceState<Bitmap?>(initialValue = null, key1 = perfil.fotoBase64) {
        value = withContext(Dispatchers.Default) {
            try {
                if (!perfil.fotoBase64.isNullOrBlank()) {
                    val bytes = Base64.decode(perfil.fotoBase64, Base64.NO_WRAP)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } else null
            } catch (e: Exception) {
                Log.e("PERFIL", "Error decodificando foto base64", e)
                null
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .offset(y = 44.dp)
                    .size(128.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = bitmap, label = "avatarCrossfade") { bmp ->
                    if (bmp != null) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
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

            Box(
                modifier = Modifier
                    .offset(y = 44.dp)
                    .size(128.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Cambiar foto")
                }
            }
        }

        Spacer(Modifier.height(56.dp))

        Text(
            text = perfil.nombre,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        AssistChip(
            onClick = {},
            label = { Text(if (perfil.rol == Rol.ADMIN) "Administrador" else "Usuario") },
            leadingIcon = {
                Icon(
                    imageVector = if (perfil.rol == Rol.ADMIN) Icons.Default.Star else Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Cuenta", style = MaterialTheme.typography.titleMedium)

                        ListItem(
                            headlineContent = { Text(perfil.correo) },
                            overlineContent = { Text("Correo electrónico") },
                            leadingContent = {
                                Icon(Icons.Default.Email, contentDescription = null)
                            }
                        )

                        OutlinedTextField(
                            value = nombreInput,
                            onValueChange = { if (it.length <= 30) nombreInput = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            enabled = !guardando,
                            supportingText = { Text("${nombreInput.length}/30") },
                            trailingIcon = {
                                IconButton(
                                    onClick = { onGuardarNombre(nombreInput) },
                                    enabled = !guardando && nombreInput.trim() != perfil.nombre && nombreInput.isNotBlank()
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Guardar nombre")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Apariencia", style = MaterialTheme.typography.titleMedium)
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
                }

                AnimatedVisibility(
                    visible = guardando,
                    enter = fadeIn(),
                    exit = Q@fadeOut()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardando...", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                FilledTonalButton(
                    onClick = { mostrarDialogoCerrarSesion = true },
                    enabled = !guardando,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
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

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

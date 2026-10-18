package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.components.CafeButton
import com.yaycate.cafecampus.ui.components.CafeTextField
import com.yaycate.cafecampus.ui.theme.CafeTheme
import com.yaycate.cafecampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(vm: SesionViewModel, onIngreso: (Rol) -> Unit) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()

    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // Cuando el login es correcto, avisa a la navegación con el rol del usuario
    LaunchedEffect(usuario) {
        usuario?.let { onIngreso(Rol.valueOf(it.rol)) }
    }

    // El tema cambia de color según la pestaña elegida
    CafeTheme(rol = rol) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(32.dp))

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ingresa para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            TabRow(selectedTabIndex = if (rol == Rol.CLIENTE) 0 else 1) {
                Tab(
                    selected = rol == Rol.CLIENTE,
                    onClick = { rol = Rol.CLIENTE },
                    text = { Text("Estudiante") }
                )
                Tab(
                    selected = rol == Rol.ADMIN,
                    onClick = { rol = Rol.ADMIN },
                    text = { Text("Administrador") }
                )
            }

            Spacer(Modifier.height(24.dp))

            CafeTextField(
                valor = correo,
                onValorChange = { correo = it },
                etiqueta = "Correo"
            )

            Spacer(Modifier.height(12.dp))

            CafeTextField(
                valor = clave,
                onValorChange = { clave = it },
                etiqueta = "Contraseña",
                esClave = true
            )

            Spacer(Modifier.height(12.dp))

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            CafeButton(
                texto = "Ingresar",
                onClick = { vm.login(correo, clave, rol) }
            )

            if (rol == Rol.ADMIN) {
                Spacer(Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Modo práctico",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}
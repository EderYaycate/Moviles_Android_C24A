package com.yaycate.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yaycate.cafecampus.data.prefs.PreferenciasStore
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.theme.CafeTheme
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(prefs: PreferenciasStore, onListo: (Rol?) -> Unit) {
    // Espera el perfil guardado y avisa a la navegación una sola vez
    LaunchedEffect(Unit) {
        val perfil = prefs.perfilActivo.first()
        onListo(if (perfil.isBlank()) null else Rol.valueOf(perfil))
    }

    CafeTheme(rol = Rol.CLIENTE) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("CafeCampus", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
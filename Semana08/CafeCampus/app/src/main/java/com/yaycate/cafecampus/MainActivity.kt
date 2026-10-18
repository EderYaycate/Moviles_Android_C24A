package com.yaycate.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.yaycate.cafecampus.data.prefs.PreferenciasStore
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.navigation.NavCafe
import com.yaycate.cafecampus.ui.theme.CafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRaiz()
        }
    }
}

@Composable
private fun AppRaiz() {
    val context = LocalContext.current
    val prefs = remember { PreferenciasStore(context) }

    // Las preferencias deciden el tema: modo oscuro y colores según el perfil
    val modoOscuro by prefs.modoOscuro.collectAsState(initial = false)
    val perfil by prefs.perfilActivo.collectAsState(initial = "")
    val rol = if (perfil == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

    CafeTheme(rol = rol, modoOscuro = modoOscuro) {
        NavCafe()
    }
}
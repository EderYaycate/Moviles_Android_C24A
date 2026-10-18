package com.yaycate.cafecampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.yaycate.cafecampus.model.Rol

private fun esquemaClaro(rol: Rol) =
    if (rol == Rol.ADMIN) {
        lightColorScheme(
            primary = IndigoPrimario,
            primaryContainer = IndigoContenedor,
            background = FondoAdmin,
            surface = FondoAdmin
        )
    } else {
        lightColorScheme(
            primary = NaranjaPrimario,
            primaryContainer = NaranjaContenedor,
            background = FondoCliente,
            surface = FondoCliente
        )
    }

private fun esquemaOscuro(rol: Rol) =
    if (rol == Rol.ADMIN) {
        darkColorScheme(primary = IndigoPrimario, primaryContainer = IndigoContenedor)
    } else {
        darkColorScheme(primary = NaranjaPrimario, primaryContainer = NaranjaContenedor)
    }

@Composable
fun CafeTheme(
    rol: Rol,
    modoOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (modoOscuro) esquemaOscuro(rol) else esquemaClaro(rol),
        typography = Typography,
        content = content
    )
}

// Alias temporal para MainActivity (se quita en el paso 6.x)
@Composable
fun CafeCampusTheme(content: @Composable () -> Unit) {
    CafeTheme(rol = Rol.CLIENTE, content = content)
}
package com.yaycate.animetec.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = AnimeRojo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = Color(0xFF410001),
    secondary = TecAzul,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6E3FF),
    onSecondaryContainer = Color(0xFF001C38),
    tertiary = TecAzul,
    onTertiary = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = AnimeRojoOscuro,
    onPrimary = Color(0xFF60140F),
    primaryContainer = Color(0xFF8C3833),
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = TecAzulOscuro,
    onSecondary = Color(0xFF002F6C),
    secondaryContainer = Color(0xFF004494),
    onSecondaryContainer = Color(0xFFD6E3FF),
    tertiary = TecAzulOscuro,
    onTertiary = Color(0xFF002F6C)
)

@Composable
fun AnimeTecTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

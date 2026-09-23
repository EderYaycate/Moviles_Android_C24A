import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Paleta de colores Académica
val PurpleDark = Color(0xFF2E1A47)
val PurpleMedium = Color(0xFF4A2E80)
val PurpleLight = Color(0xFF6B46C1)
val PurpleAccent = Color(0xFF9F7AEA)
val BackgroundLight = Color(0xFFF7F8FA)
val SurfaceWhite = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF1A202C)
val TextSecondary = Color(0xFF718096)

// Degradado para las pantallas de Inicio y Perfil
val AcademicGradient = Brush.verticalGradient(
    colors = listOf(PurpleDark, PurpleMedium, PurpleLight)
)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Agregar versión del plugin de Google Services para Firebase
    id("com.google.gms.google-services") version "4.4.2" apply false
}
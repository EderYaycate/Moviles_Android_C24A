package com.yaycate.animetec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yaycate.animetec.ui.navigation.AppNav
import com.yaycate.animetec.ui.theme.AnimeTecTheme
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaycate.animetec.ui.ViewModelFactory
import com.yaycate.animetec.ui.theme.ModoTema
import com.yaycate.animetec.ui.theme.TemaViewModel

class MainActivity : ComponentActivity() {
    private val pedirPermisoNotificaciones =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* si lo niega, la app sigue normal */ }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            pedirPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        enableEdgeToEdge()
        setContent {
            val container = (application as AnimeApp).container
            val temaVm: TemaViewModel = viewModel(factory = ViewModelFactory(container))
            val modo by temaVm.modo.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (modo) {
                ModoTema.SISTEMA -> systemDark
                ModoTema.CLARO -> false
                ModoTema.OSCURO -> true
            }

            AnimeTecTheme(darkTheme = darkTheme) {
                AppNav(
                    container = container,
                    modoTema = modo,
                    onCambiarModo = temaVm::cambiar
                )
            }
        }
    }
}

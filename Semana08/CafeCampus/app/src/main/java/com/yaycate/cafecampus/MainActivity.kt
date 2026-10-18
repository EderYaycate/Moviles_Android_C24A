package com.yaycate.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yaycate.cafecampus.model.Rol
import com.yaycate.cafecampus.ui.navigation.NavCafe
import com.yaycate.cafecampus.ui.theme.CafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeTheme(rol = Rol.CLIENTE) {
                NavCafe()
            }
        }
    }
}
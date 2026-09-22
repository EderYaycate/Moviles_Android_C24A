package com.yaycate.navegaciontec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yaycate.navegaciontec.navigation.AppNavigation
import com.yaycate.navegaciontec.ui.theme.NavegacionTecTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavegacionTecTheme(dynamicColor = false) {
                AppNavigation()
            }
        }}}
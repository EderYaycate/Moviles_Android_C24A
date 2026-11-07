package com.yaycate.animetec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yaycate.animetec.ui.navigation.AppNav
import com.yaycate.animetec.ui.theme.AnimeTecTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeTecTheme {
                AppNav((application as AnimeApp).container)
            }
        }
    }
}
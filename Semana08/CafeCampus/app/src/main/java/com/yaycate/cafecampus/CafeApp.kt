package com.yaycate.cafecampus

import android.app.Application
import com.yaycate.cafecampus.data.local.CafeDatabase
import com.yaycate.cafecampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CafeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(CafeDatabase.obtener(this@CafeApp))
        }
    }
}
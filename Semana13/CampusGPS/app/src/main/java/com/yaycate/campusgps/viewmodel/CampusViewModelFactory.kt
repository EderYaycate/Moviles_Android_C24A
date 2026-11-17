package com.yaycate.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaycate.campusgps.data.LugaresRepositoryLocal
import com.yaycate.campusgps.data.UbicacionRepositoryGoogle

class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapaViewModel(
            ubicacionRepo = UbicacionRepositoryGoogle(context.applicationContext),
            lugaresRepo = LugaresRepositoryLocal()
        ) as T
    }
}
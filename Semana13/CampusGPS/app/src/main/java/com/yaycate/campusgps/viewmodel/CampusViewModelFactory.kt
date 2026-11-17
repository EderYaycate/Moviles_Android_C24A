package com.yaycate.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaycate.campusgps.data.LugaresRepositoryLocal
import com.yaycate.campusgps.data.TemaRepositoryDataStore
import com.yaycate.campusgps.data.UbicacionRepositoryGoogle
import com.yaycate.campusgps.data.UltimoLugarRepositoryDataStore

class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext
        return MapaViewModel(
            ubicacionRepo = UbicacionRepositoryGoogle(appContext),
            lugaresRepo = LugaresRepositoryLocal(),
            ultimoLugarRepo = UltimoLugarRepositoryDataStore(appContext),
            temaRepo = TemaRepositoryDataStore(appContext)
        ) as T
    }
}
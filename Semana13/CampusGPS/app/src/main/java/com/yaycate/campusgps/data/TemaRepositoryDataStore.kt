package com.yaycate.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStoreTema: DataStore<Preferences> by preferencesDataStore(name = "tema_prefs")

class TemaRepositoryDataStore(
    private val context: Context
) : TemaRepository {

    private companion object {
        val LLAVE_MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
    }

    override val modoOscuro: Flow<Boolean> = context.dataStoreTema.data
        .map { preferences -> preferences[LLAVE_MODO_OSCURO] ?: false }

    override suspend fun guardarModoOscuro(oscuro: Boolean) {
        context.dataStoreTema.edit { preferences ->
            preferences[LLAVE_MODO_OSCURO] = oscuro
        }
    }
}
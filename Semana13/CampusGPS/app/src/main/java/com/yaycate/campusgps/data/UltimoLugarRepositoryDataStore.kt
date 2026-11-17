package com.yaycate.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ultimo_lugar_prefs")

class UltimoLugarRepositoryDataStore(
    private val context: Context
) : UltimoLugarRepository {

    private companion object {
        val LLAVE_ULTIMO_LUGAR = intPreferencesKey("ultimo_lugar_id")
    }

    override val ultimoLugar: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[LLAVE_ULTIMO_LUGAR]
        }

    override suspend fun guardarUltimo(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[LLAVE_ULTIMO_LUGAR] = id
        }
    }
}

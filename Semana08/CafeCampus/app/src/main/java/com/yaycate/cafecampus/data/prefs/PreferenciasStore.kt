package com.yaycate.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yaycate.cafecampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión declarada una sola vez, a nivel de archivo
private val Context.dataStore by preferencesDataStore("ajustes")

class PreferenciasStore(private val context: Context) {

    private val perfilKey = stringPreferencesKey("perfilActivo")
    private val modoOscuroKey = booleanPreferencesKey("modoOscuro")
    private val recibirKey = booleanPreferencesKey("recibirPedidos")

    // Lectura (Flow, una por preferencia)
    val perfilActivo: Flow<String> =
        context.dataStore.data.map { it[perfilKey] ?: "" }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { it[modoOscuroKey] ?: false }

    val recibirPedidos: Flow<Boolean> =
        context.dataStore.data.map { it[recibirKey] ?: true }

    // Escritura (suspend)
    suspend fun guardarPerfil(rol: Rol) {
        context.dataStore.edit { it[perfilKey] = rol.name }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { it[modoOscuroKey] = b }
    }

    suspend fun guardarRecibirPedidos(b: Boolean) {
        context.dataStore.edit { it[recibirKey] = b }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { it[perfilKey] = "" }
    }
}
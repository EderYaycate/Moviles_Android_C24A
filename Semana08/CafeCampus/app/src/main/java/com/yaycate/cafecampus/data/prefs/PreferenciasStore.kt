package com.yaycate.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yaycate.cafecampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión declarada una sola vez, a nivel de archivo
private val Context.dataStore by preferencesDataStore("ajustes")

class PreferenciasStore(private val context: Context) {

    private val perfilKey = stringPreferencesKey("perfilActivo")
    private val clienteIdKey = intPreferencesKey("clienteId")
    private val modoOscuroKey = booleanPreferencesKey("modoOscuro")
    private val recibirKey = booleanPreferencesKey("recibirPedidos")
    private val notificacionesKey = booleanPreferencesKey("notificaciones")
    private val favoritosKey = stringSetPreferencesKey("favoritos")

    // Lectura (Flow, una por preferencia)
    val perfilActivo: Flow<String> =
        context.dataStore.data.map { it[perfilKey] ?: "" }

    // -1 significa que no hay sesión guardada
    val clienteId: Flow<Int> =
        context.dataStore.data.map { it[clienteIdKey] ?: -1 }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { it[modoOscuroKey] ?: false }

    val recibirPedidos: Flow<Boolean> =
        context.dataStore.data.map { it[recibirKey] ?: true }

    // Avisos de nuevos pedidos para el administrador
    val notificaciones: Flow<Boolean> =
        context.dataStore.data.map { it[notificacionesKey] ?: true }

    // Conjunto de ids de productos favoritos
    val favoritos: Flow<Set<Int>> =
        context.dataStore.data.map { prefs ->
            prefs[favoritosKey]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }

    // Escritura (suspend)
    suspend fun guardarPerfil(rol: Rol) {
        context.dataStore.edit { it[perfilKey] = rol.name }
    }

    suspend fun guardarClienteId(id: Int) {
        context.dataStore.edit { it[clienteIdKey] = id }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { it[modoOscuroKey] = b }
    }

    suspend fun guardarRecibirPedidos(b: Boolean) {
        context.dataStore.edit { it[recibirKey] = b }
    }

    suspend fun guardarNotificaciones(b: Boolean) {
        context.dataStore.edit { it[notificacionesKey] = b }
    }

    suspend fun toggleFavorito(id: Int) {
        context.dataStore.edit { prefs ->
            val actuales = prefs[favoritosKey]?.toMutableSet() ?: mutableSetOf()
            val idStr = id.toString()
            if (actuales.contains(idStr)) {
                actuales.remove(idStr)
            } else {
                actuales.add(idStr)
            }
            prefs[favoritosKey] = actuales
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it[perfilKey] = ""
            it[clienteIdKey] = -1
        }
    }
}
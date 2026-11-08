package com.yaycate.animetec.data.novedades

import android.content.Context
import android.content.SharedPreferences

class LecturaNovedadesPrefs(
    private val context: Context
) : LecturaNovedades {

    private val prefs: SharedPreferences by lazy {
        context.applicationContext.getSharedPreferences("novedades_prefs", Context.MODE_PRIVATE)
    }

    override fun ultimaVista(uid: String): Long {
        return prefs.getLong("ultima_vista_$uid", 0L)
    }

    override fun marcarVistas(uid: String, hasta: Long) {
        prefs.edit().putLong("ultima_vista_$uid", hasta).apply()
    }
}

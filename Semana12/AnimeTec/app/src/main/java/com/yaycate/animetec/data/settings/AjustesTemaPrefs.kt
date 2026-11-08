package com.yaycate.animetec.data.settings

import android.content.Context
import android.content.SharedPreferences
import com.yaycate.animetec.ui.theme.ModoTema

class AjustesTemaPrefs(
    private val context: Context
) : AjustesTema {

    private val prefs: SharedPreferences by lazy {
        context.applicationContext.getSharedPreferences("ajustes_prefs", Context.MODE_PRIVATE)
    }

    override fun leer(): ModoTema {
        val name = prefs.getString("modo_tema", ModoTema.SISTEMA.name)
        return try {
            ModoTema.valueOf(name ?: ModoTema.SISTEMA.name)
        } catch (e: Exception) {
            ModoTema.SISTEMA
        }
    }

    override fun guardar(modo: ModoTema) {
        prefs.edit().putString("modo_tema", modo.name).apply()
    }
}

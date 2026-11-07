package com.yaycate.animetec.notification

import android.util.Log

// Temporal: se reemplaza por NotificadorAndroid en H15
class NotificadorLog : Notificador {
    override fun mostrar(titulo: String, texto: String) {
        Log.d("NOTIF", "$titulo: $texto")
    }
}
package com.yaycate.animetec.fakes

import com.yaycate.animetec.notification.Notificador

class FakeNotificador : Notificador {

    val mensajes = mutableListOf<Pair<String, String>>()

    override fun mostrar(titulo: String, texto: String) {
        mensajes.add(Pair(titulo, texto))
    }
}

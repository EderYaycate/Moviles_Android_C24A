package com.yaycate.animetec.ui.lista

import com.yaycate.animetec.domain.model.Item

fun List<Item>.filtrarPorTitulo(consulta: String): List<Item> {
    val trimmed = consulta.trim()
    if (trimmed.isEmpty()) return this
    return this.filter { it.titulo.contains(trimmed, ignoreCase = true) }
}

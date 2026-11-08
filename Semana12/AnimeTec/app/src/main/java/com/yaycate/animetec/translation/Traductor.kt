package com.yaycate.animetec.translation

interface Traductor {
    suspend fun traducirAEspanol(texto: String): Result<String>
}

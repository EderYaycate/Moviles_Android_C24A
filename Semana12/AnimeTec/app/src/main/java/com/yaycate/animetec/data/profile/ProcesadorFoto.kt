package com.yaycate.animetec.data.profile

interface ProcesadorFoto {
    suspend fun aBase64(uri: String): Result<String>
}

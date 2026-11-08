package com.yaycate.animetec.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException
import kotlin.getValue

class TraductorMlKit : Traductor {

    private val translator by lazy {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.SPANISH)
            .build()
        Translation.getClient(options)
    }

    override suspend fun traducirAEspanol(texto: String): Result<String> {
        if (texto.isBlank()) {
            return Result.success(texto)
        }
        return try {
            val conditions = DownloadConditions.Builder().build()
            translator.downloadModelIfNeeded(conditions).await()
            val traducido = translator.translate(texto).await()
            Result.success(traducido)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

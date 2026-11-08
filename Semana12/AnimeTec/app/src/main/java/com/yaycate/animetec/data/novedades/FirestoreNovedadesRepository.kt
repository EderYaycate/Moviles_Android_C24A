package com.yaycate.animetec.data.novedades

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yaycate.animetec.domain.model.Novedad
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreNovedadesRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : NovedadesRepository {

    override fun observar(): Flow<List<Novedad>> = callbackFlow {
        val listener = db.collection("novedades")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val lista = snapshot.documents.mapNotNull { doc ->
                        try {
                            val id = doc.id
                            val titulo = doc.getString("titulo") ?: return@mapNotNull null
                            val mensaje = doc.getString("mensaje") ?: return@mapNotNull null
                            val animeId = doc.getLong("animeId")?.toInt()
                            val fecha = doc.getLong("fecha") ?: return@mapNotNull null
                            Novedad(id, titulo, mensaje, animeId, fecha)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(lista)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun publicar(titulo: String, mensaje: String, animeId: Int?): Result<Unit> = try {
        val data = mutableMapOf<String, Any>(
            "titulo" to titulo,
            "mensaje" to mensaje,
            "fecha" to System.currentTimeMillis()
        )
        if (animeId != null) {
            data["animeId"] = animeId
        }
        db.collection("novedades").add(data).await()
        Result.success(Unit)
    } catch (e: Exception) {
        val msg = if (e.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true ||
            e.message?.contains("permission") == true) {
            "No tienes permiso para publicar"
        } else {
            e.message ?: "Error al publicar novedad"
        }
        Result.failure(Exception(msg))
    }

    override suspend fun eliminar(id: String): Result<Unit> = try {
        db.collection("novedades").document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

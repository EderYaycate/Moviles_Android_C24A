package com.yaycate.animetec.data.novedades

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yaycate.animetec.domain.model.Novedad
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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
}

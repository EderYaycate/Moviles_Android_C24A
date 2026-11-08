package com.yaycate.animetec.data.profile

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yaycate.animetec.domain.model.Perfil
import com.yaycate.animetec.domain.model.Rol
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirestorePerfilRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : PerfilRepository {

    override suspend fun obtener(uid: String): Result<Perfil> = try {
        val doc = db.collection("usuarios").document(uid).get().await()
        val correo = doc.getString("correo") ?: doc.getString("email") ?: auth.currentUser?.email ?: ""
        val nombreGuardado = doc.getString("nombre")
        val nombre = if (!nombreGuardado.isNullOrBlank()) {
            nombreGuardado
        } else {
            correo.substringBefore("@").ifBlank { "Usuario" }
        }
        val rol = if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        val foto = doc.getString("fotoBase64") ?: doc.getString("foto")
        Result.success(Perfil(nombre = nombre, correo = correo, rol = rol, fotoBase64 = foto))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e("PERFIL", "Error al obtener perfil", e)
        val msg = if (e.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true ||
            e.message?.contains("permission") == true) {
            "No tienes permiso para guardar. Revisa las reglas de Firestore"
        } else {
            e.message ?: "Error al obtener perfil"
        }
        Result.failure(Exception(msg))
    }

    override suspend fun guardarNombre(uid: String, nombre: String): Result<Unit> =
        guardarCampo(uid, "nombre", nombre)

    override suspend fun guardarFoto(uid: String, fotoBase64: String): Result<Unit> {
        val docRef = db.collection("usuarios").document(uid)
        return try {
            val doc = docRef.get().await()
            if (doc.exists()) {
                docRef.update("fotoBase64", fotoBase64).await()
            } else {
                docRef.set(mapOf("fotoBase64" to fotoBase64, "rol" to "usuario")).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PERFIL", "Error al guardar foto", e)
            val msg = if (e.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true ||
                e.message?.contains("permission") == true) {
                "No tienes permiso para guardar. Revisa las reglas de Firestore"
            } else {
                e.message ?: "Error al guardar foto"
            }
            Result.failure(Exception(msg))
        }
    }

    private suspend fun guardarCampo(uid: String, campo: String, valor: Any): Result<Unit> {
        val docRef = db.collection("usuarios").document(uid)
        return try {
            val doc = docRef.get().await()
            if (doc.exists()) {
                docRef.update(campo, valor).await()
            } else {
                docRef.set(mapOf(campo to valor, "rol" to "usuario")).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PERFIL", "Error al guardar campo $campo", e)
            val msg = if (e.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true ||
                e.message?.contains("permission") == true) {
                "No tienes permiso para guardar. Revisa las reglas de Firestore"
            } else {
                e.message ?: "Error al guardar perfil"
            }
            Result.failure(Exception(msg))
        }
    }
}

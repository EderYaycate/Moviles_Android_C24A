package com.yaycate.animetec.data.profile

import com.google.firebase.FirebaseNetworkException
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
        val foto = doc.getString("foto") ?: doc.getString("fotoBase64")
        Result.success(Perfil(nombre = nombre, correo = correo, rol = rol, fotoBase64 = foto))
    } catch (e: CancellationException) {
        throw e
    } catch (e: FirebaseNetworkException) {
        Result.failure(Exception("No hay conexión a internet"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun guardarNombre(uid: String, nombre: String): Result<Unit> =
        guardarCampo(uid, "nombre", nombre)

    override suspend fun guardarFoto(uid: String, fotoBase64: String): Result<Unit> {
        val docRef = db.collection("usuarios").document(uid)
        return try {
            val doc = docRef.get().await()
            if (doc.exists()) {
                docRef.update(mapOf("foto" to fotoBase64, "fotoBase64" to fotoBase64)).await()
            } else {
                docRef.set(mapOf("foto" to fotoBase64, "fotoBase64" to fotoBase64, "rol" to "usuario")).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseNetworkException) {
            Result.failure(Exception("No hay conexión a internet"))
        } catch (e: Exception) {
            Result.failure(e)
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
        } catch (e: FirebaseNetworkException) {
            Result.failure(Exception("No hay conexión a internet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

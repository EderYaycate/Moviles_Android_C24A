package com.yaycate.animetec.data.auth

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.yaycate.animetec.domain.model.Rol
import com.yaycate.animetec.domain.model.Sesion
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val uid = auth.signInWithEmailAndPassword(correo, clave).await().user!!.uid
        Result.success(leerSesion(uid, correo))
    } catch (e: CancellationException) {
        throw e
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(CredencialesInvalidasException())
    } catch (e: FirebaseAuthInvalidUserException) {
        Result.failure(CredencialesInvalidasException())
    } catch (e: FirebaseNetworkException) {
        Result.failure(SinConexionException())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sesionActual(): Sesion? {
        val u = auth.currentUser ?: return null
        return try {
            leerSesion(u.uid, u.email ?: "")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override fun cerrarSesion() = auth.signOut()

    override suspend fun recuperarClave(correo: String): Result<Unit> = try {
        val correoClean = correo.trim().lowercase()
        auth.useAppLanguage()
        auth.sendPasswordResetEmail(correoClean).await()
        Result.success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: FirebaseAuthInvalidUserException) {
        // Éxito neutro para no revelar si el correo existe
        Result.success(Unit)
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(Exception("El correo no tiene un formato válido"))
    } catch (e: FirebaseTooManyRequestsException) {
        Result.failure(Exception("Demasiados intentos. Espera unos minutos e inténtalo de nuevo"))
    } catch (e: FirebaseNetworkException) {
        Result.failure(SinConexionException())
    } catch (e: Exception) {
        Log.e("AUTH", "recuperarClave", e)
        Result.failure(Exception("No se pudo enviar el correo. Inténtalo de nuevo"))
    }

    override suspend fun registrar(correo: String, clave: String): Result<Sesion> = try {
        val uid = auth.createUserWithEmailAndPassword(correo, clave).await().user!!.uid
        Result.success(leerSesion(uid, correo))
    } catch (e: CancellationException) {
        throw e
    } catch (e: FirebaseAuthUserCollisionException) {
        Result.failure(CorreoYaRegistradoException())
    } catch (e: FirebaseAuthWeakPasswordException) {
        Result.failure(ClaveDebilException())
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(CorreoInvalidoException())
    } catch (e: FirebaseNetworkException) {
        Result.failure(SinConexionException())
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val rol = try {
            val doc = db.collection("usuarios").document(uid).get().await()
            if (doc.exists() && doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        } catch (e: Exception) {
            Rol.USUARIO
        }
        return Sesion(uid, correo, rol)
    }
}

package com.yaycate.animetec

import android.content.Context
import com.yaycate.animetec.data.auth.AuthRepository
import com.yaycate.animetec.data.auth.FirebaseAuthRepository
import com.yaycate.animetec.data.local.AppDatabase
import com.yaycate.animetec.data.profile.FirestorePerfilRepository
import com.yaycate.animetec.data.profile.PerfilRepository
import com.yaycate.animetec.data.profile.ProcesadorFoto
import com.yaycate.animetec.data.profile.ProcesadorFotoAndroid
import com.yaycate.animetec.data.remote.RetrofitClient
import com.yaycate.animetec.data.repository.ItemRepository
import com.yaycate.animetec.data.repository.ItemRepositoryImpl
import com.yaycate.animetec.notification.Notificador
import com.yaycate.animetec.notification.NotificadorAndroid
import com.yaycate.animetec.translation.Traductor
import com.yaycate.animetec.translation.TraductorMlKit

class AppContainer(private val context: Context) {

    private val database: AppDatabase by lazy { AppDatabase.crear(context.applicationContext) }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.api, database.itemDao())
    }

    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }
    val notificador: Notificador by lazy { NotificadorAndroid(context.applicationContext) }
    val traductor: Traductor by lazy { TraductorMlKit() }
    val perfilRepository: PerfilRepository by lazy { FirestorePerfilRepository() }
    val procesadorFoto: ProcesadorFoto by lazy { ProcesadorFotoAndroid(context.applicationContext) }
}

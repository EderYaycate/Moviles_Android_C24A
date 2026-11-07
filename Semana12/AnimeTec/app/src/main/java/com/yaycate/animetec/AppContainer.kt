package com.yaycate.animetec

import android.content.Context
import com.yaycate.animetec.data.local.AppDatabase
import com.yaycate.animetec.data.remote.RetrofitClient
import com.yaycate.animetec.data.repository.ItemRepository
import com.yaycate.animetec.data.repository.ItemRepositoryImpl
import com.yaycate.animetec.notification.Notificador
import com.yaycate.animetec.notification.NotificadorLog

class AppContainer(private val context: Context) {

    private val database: AppDatabase by lazy { AppDatabase.crear(context.applicationContext) }

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(RetrofitClient.api, database.itemDao())
    }

    val notificador: Notificador by lazy { NotificadorLog() }
}
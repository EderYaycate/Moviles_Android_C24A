package com.yaycate.animetec

import com.yaycate.animetec.data.remote.RetrofitClient
import com.yaycate.animetec.data.repository.ItemRepository
import com.yaycate.animetec.data.repository.ItemRepositoryImpl
import com.yaycate.animetec.notification.Notificador
import com.yaycate.animetec.notification.NotificadorLog

class AppContainer {
    val itemRepository: ItemRepository by lazy { ItemRepositoryImpl(RetrofitClient.api) }
    val notificador: Notificador by lazy { NotificadorLog() }
}
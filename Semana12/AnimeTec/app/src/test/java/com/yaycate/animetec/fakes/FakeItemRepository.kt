package com.yaycate.animetec.fakes

import com.yaycate.animetec.domain.model.Item
import com.yaycate.animetec.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeItemRepository : ItemRepository {

    val guardados = MutableStateFlow<List<Item>>(emptyList())
    var resultadoRefrescar: Result<Int> = Result.success(0)
    var itemsTrasRefrescar: List<Item>? = null
    var llamadasRefrescar: Int = 0

    override val items: Flow<List<Item>> = guardados

    override val favoritos: Flow<List<Item>> = guardados.map { lista ->
        lista.filter { it.esFavorito }
    }

    override suspend fun refrescar(): Result<Int> {
        llamadasRefrescar++
        if (resultadoRefrescar.isSuccess) {
            itemsTrasRefrescar?.let {
                guardados.value = it
            }
        }
        return resultadoRefrescar
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val item = guardados.value.find { it.id == id }
        return if (item != null) {
            Result.success(item)
        } else {
            Result.failure(Exception("Item no encontrado"))
        }
    }

    override suspend fun alternarFavorito(id: Int) {
        guardados.value = guardados.value.map { item ->
            if (item.id == id) {
                item.copy(esFavorito = !item.esFavorito)
            } else {
                item
            }
        }
    }

    override suspend fun cantidadGuardada(): Int {
        return guardados.value.size
    }

    override suspend fun cantidadFavoritos(): Int {
        return guardados.value.count { it.esFavorito }
    }

    override suspend fun limpiarCache() {
        guardados.value = emptyList()
    }
}

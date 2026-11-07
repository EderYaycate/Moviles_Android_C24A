package com.yaycate.animetec.data.repository

import com.yaycate.animetec.data.mapper.aModelo
import com.yaycate.animetec.data.remote.ApiService
import com.yaycate.animetec.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

// Versión temporal: guarda en memoria. En H8 se reemplaza por Room.
class ItemRepositoryImpl(private val api: ApiService) : ItemRepository {

    private val guardados = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = guardados.asStateFlow()
    override val favoritos: Flow<List<Item>> = guardados.map { l -> l.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = try {
        // Conservar favoritos al sincronizar
        val idsFavoritos = guardados.value.filter { it.esFavorito }.map { it.id }.toSet()
        val nuevos = api.listar().data
            .map { it.aModelo() }
            .map { it.copy(esFavorito = it.id in idsFavoritos) }
        guardados.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        Result.success(api.detalle(id).data.aModelo())
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        guardados.value = guardados.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    override suspend fun cantidadGuardada() = guardados.value.size
    override suspend fun cantidadFavoritos() = guardados.value.count { it.esFavorito }
    override suspend fun limpiarCache() { guardados.value = emptyList() }
}
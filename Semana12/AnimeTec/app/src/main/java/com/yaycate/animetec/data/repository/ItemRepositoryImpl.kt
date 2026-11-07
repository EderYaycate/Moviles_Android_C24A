package com.yaycate.animetec.data.repository

import com.yaycate.animetec.data.local.ItemDao
import com.yaycate.animetec.data.mapper.aEntity
import com.yaycate.animetec.data.mapper.aModelo
import com.yaycate.animetec.data.remote.ApiService
import com.yaycate.animetec.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    // Room es la fuente única de verdad: la UI siempre lee de aquí
    override val items: Flow<List<Item>> = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // Si guardas sin cuidado, pierdes los favoritos. Se conservan aquí:
        val idsFavoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().data
            .map { dto -> dto.aEntity(esFavorito = dto.id.toInt() in idsFavoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {            // sin internet
        Result.failure(e)
    } catch (e: HttpException) {          // 404, 429, 500...
        Result.failure(e)
    } catch (e: SerializationException) { // JSON inesperado
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

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
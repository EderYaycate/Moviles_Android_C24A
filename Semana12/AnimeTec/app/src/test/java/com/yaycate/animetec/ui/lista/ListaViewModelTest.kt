package com.yaycate.animetec.ui.lista

import com.yaycate.animetec.MainDispatcherRule
import com.yaycate.animetec.domain.model.Item
import com.yaycate.animetec.fakes.FakeItemRepository
import com.yaycate.animetec.fakes.FakeNotificador
import com.yaycate.animetec.ui.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun refrescoExitoso_actualizaEstadoYNotifica() = runTest {
        val repo = FakeItemRepository()
        val notificador = FakeNotificador()
        val itemsPrueba = listOf(
            Item(1, "Anime 1", "Sub 1", imagenUrl = null),
            Item(2, "Anime 2", "Sub 2", imagenUrl = null)
        )
        repo.resultadoRefrescar = Result.success(itemsPrueba.size)
        repo.itemsTrasRefrescar = itemsPrueba

        val viewModel = ListaViewModel(repo, notificador)

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(itemsPrueba, (estado as UiState.Exito).datos)
        assertEquals(1, notificador.mensajes.size)
        assertEquals("Datos actualizados", notificador.mensajes[0].first)
        assertEquals("Se guardaron 2 elementos", notificador.mensajes[0].second)
    }

    @Test
    fun ioException_sinDatosGuardados_muestraError() = runTest {
        val repo = FakeItemRepository()
        val notificador = FakeNotificador()
        repo.resultadoRefrescar = Result.failure(IOException())
        repo.itemsTrasRefrescar = emptyList()
        repo.guardados.value = emptyList()

        val viewModel = ListaViewModel(repo, notificador)

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Error)
        assertEquals("No hay conexión a internet", (estado as UiState.Error).mensaje)
        assertTrue(notificador.mensajes.isEmpty())
    }

    @Test
    fun ioException_conDatosGuardados_muestraAvisoYMantieneExito() = runTest {
        val repo = FakeItemRepository()
        val notificador = FakeNotificador()
        val itemsPrueba = listOf(
            Item(1, "Anime 1", "Sub 1", imagenUrl = null)
        )
        repo.guardados.value = itemsPrueba
        repo.resultadoRefrescar = Result.failure(IOException())

        val viewModel = ListaViewModel(repo, notificador)

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(itemsPrueba, (estado as UiState.Exito).datos)
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
        assertTrue(notificador.mensajes.isEmpty())
    }

    @Test
    fun reintentarDespuesDeError_exitoso_dejaEstadoEnExito() = runTest {
        val repo = FakeItemRepository()
        val notificador = FakeNotificador()
        repo.resultadoRefrescar = Result.failure(IOException())
        repo.guardados.value = emptyList()

        val viewModel = ListaViewModel(repo, notificador)
        assertTrue(viewModel.estado.value is UiState.Error)

        val itemsPrueba = listOf(
            Item(1, "Anime 1", "Sub 1", imagenUrl = null)
        )
        repo.resultadoRefrescar = Result.success(1)
        repo.itemsTrasRefrescar = itemsPrueba

        viewModel.refrescar()

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(itemsPrueba, (estado as UiState.Exito).datos)
    }

    @Test
    fun alternarFavorito_marcaItemComoFavorito() = runTest {
        val repo = FakeItemRepository()
        val notificador = FakeNotificador()
        val item = Item(1, "Anime 1", "Sub 1", imagenUrl = null, esFavorito = false)
        repo.guardados.value = listOf(item)
        repo.resultadoRefrescar = Result.success(1)
        repo.itemsTrasRefrescar = listOf(item)

        val viewModel = ListaViewModel(repo, notificador)

        viewModel.alternarFavorito(1)

        val itemActualizado = repo.guardados.value.first { it.id == 1 }
        assertTrue(itemActualizado.esFavorito)
    }
}

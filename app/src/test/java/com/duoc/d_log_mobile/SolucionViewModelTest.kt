package com.duoc.d_log_mobile

import com.duoc.d_log_mobile.viewmodel.SolucionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SolucionViewModelTest {
    @Test
    fun catalogoTieneSeisFichasConIdsUnicos() {
        val viewModel = SolucionViewModel()
        val soluciones = viewModel.uiState.value.soluciones

        assertEquals(6, soluciones.size)
        assertEquals(6, soluciones.map { it.id }.toSet().size)
        soluciones.forEach { assertEquals(it, viewModel.obtenerSolucion(it.id)) }
        assertNull(viewModel.obtenerSolucion("id-inexistente"))
    }

    @Test
    fun busquedaYCategoriaSeCombinanSinDistinguirMayusculas() {
        val viewModel = SolucionViewModel()
        viewModel.actualizarBusqueda("MURO")
        viewModel.seleccionarCategoria("muros")
        assertEquals(2, viewModel.uiState.value.solucionesFiltradas.size)

        viewModel.seleccionarCategoria("Pisos")
        assertTrue(viewModel.uiState.value.solucionesFiltradas.isEmpty())
    }

    @Test
    fun limpiarCriteriosRecuperaElCatalogoCompleto() {
        val viewModel = SolucionViewModel()
        viewModel.actualizarBusqueda("sin coincidencias")
        viewModel.seleccionarCategoria("Muros")
        assertTrue(viewModel.uiState.value.solucionesFiltradas.isEmpty())

        viewModel.actualizarBusqueda("")
        viewModel.seleccionarCategoria("Todas")
        assertEquals(6, viewModel.uiState.value.solucionesFiltradas.size)
    }

    @Test
    fun favoritoSigueDisponibleAunqueElFiltroOculteLaFicha() {
        val viewModel = SolucionViewModel()
        val solucion = viewModel.uiState.value.soluciones.first()
        viewModel.alternarFavorito(solucion.id)
        viewModel.seleccionarCategoria("Pisos")

        assertFalse(viewModel.uiState.value.solucionesFiltradas.contains(solucion))
        assertEquals(listOf(solucion), viewModel.uiState.value.solucionesFavoritas)

        viewModel.alternarFavorito(solucion.id)
        assertTrue(viewModel.uiState.value.solucionesFavoritas.isEmpty())
    }

    @Test
    fun limpiarFavoritosNoEliminaElCatalogo() {
        val viewModel = SolucionViewModel()
        val soluciones = viewModel.uiState.value.soluciones
        soluciones.forEach { viewModel.alternarFavorito(it.id) }
        viewModel.limpiarFavoritos()

        assertTrue(viewModel.uiState.value.favoritos.isEmpty())
        assertEquals(soluciones, viewModel.uiState.value.soluciones)
    }

    @Test
    fun noSeGuardanFavoritosConIdsInexistentes() {
        val viewModel = SolucionViewModel()
        viewModel.alternarFavorito("id-inexistente")
        assertTrue(viewModel.uiState.value.favoritos.isEmpty())
    }

    @Test
    fun mensajeDeFavoritoSoloSeMuestraEnLaFichaQueLoOrigino() {
        val viewModel = SolucionViewModel()
        val primera = viewModel.uiState.value.soluciones[0].id
        val segunda = viewModel.uiState.value.soluciones[1].id

        viewModel.alternarFavorito(primera)
        assertEquals("Favorito guardado", viewModel.uiState.value.mensajeParaSolucion(primera))
        assertNull(viewModel.uiState.value.mensajeParaSolucion(segunda))

        viewModel.alternarFavorito(segunda)
        assertNull(viewModel.uiState.value.mensajeParaSolucion(primera))
        assertEquals("Favorito guardado", viewModel.uiState.value.mensajeParaSolucion(segunda))
    }

    @Test
    fun reiniciarFiltrosLimpiaConfirmacionSinBorrarFavoritos() {
        val viewModel = SolucionViewModel()
        val id = viewModel.uiState.value.soluciones.first().id
        viewModel.alternarFavorito(id)
        viewModel.reiniciarFiltros()

        assertNull(viewModel.uiState.value.mensajeParaSolucion(id))
        assertEquals(setOf(id), viewModel.uiState.value.favoritos)
    }
}

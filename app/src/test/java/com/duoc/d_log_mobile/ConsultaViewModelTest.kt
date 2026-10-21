package com.duoc.d_log_mobile

import com.duoc.d_log_mobile.model.ConsultaUiState
import com.duoc.d_log_mobile.viewmodel.ConsultaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ConsultaViewModelTest {
    @Test
    fun formularioVacioNoPermiteContinuarYMuestraLosCuatroErrores() {
        val viewModel = ConsultaViewModel()

        assertFalse(viewModel.validarFormulario())
        val errores = viewModel.uiState.value.errores
        assertNotNull(errores.nombre)
        assertNotNull(errores.correo)
        assertNotNull(errores.categoria)
        assertNotNull(errores.descripcion)
    }

    @Test
    fun espaciosNoCuentanComoCamposCompletos() {
        val viewModel = ConsultaViewModel()
        viewModel.actualizarNombre("   ")
        viewModel.actualizarCorreo("   ")
        viewModel.actualizarCategoria("   ")
        viewModel.actualizarDescripcion("             ")

        assertFalse(viewModel.validarFormulario())
        val errores = viewModel.uiState.value.errores
        assertNotNull(errores.nombre)
        assertNotNull(errores.correo)
        assertNotNull(errores.categoria)
        assertNotNull(errores.descripcion)
    }

    @Test
    fun correoInvalidoImpideContinuarAunqueLosOtrosCamposSeanValidos() {
        val viewModel = consultaCompleta()
        val correosInvalidos = listOf(
            "sin-arroba.cl", "nombre@dominio", "@correo.cl",
            "nombre@@correo.cl", "nombre apellido@correo.cl", "nombre@correo.cl extra"
        )
        correosInvalidos.forEach { correo ->
            viewModel.actualizarCorreo(correo)
            assertFalse("No deberia aceptar: $correo", viewModel.validarFormulario())
            assertNotNull(viewModel.uiState.value.errores.correo)
            assertNull(viewModel.uiState.value.errores.nombre)
            assertNull(viewModel.uiState.value.errores.categoria)
            assertNull(viewModel.uiState.value.errores.descripcion)
        }
    }

    @Test
    fun descripcionExigeDiezCaracteresSinContarEspaciosDeLosExtremos() {
        val viewModel = consultaCompleta()
        viewModel.actualizarDescripcion(" 123456789 ")
        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.uiState.value.errores.descripcion)

        viewModel.actualizarDescripcion("1234567890")
        assertTrue(viewModel.validarFormulario())
    }

    @Test
    fun formularioCorrectoPermiteContinuarYConservaDatosParaResumen() {
        val viewModel = consultaCompleta()
        val datos = viewModel.uiState.value

        assertTrue(viewModel.validarFormulario())
        assertEquals(datos, viewModel.uiState.value)
    }

    @Test
    fun editarUnCampoLimpiaSoloSuError() {
        val viewModel = ConsultaViewModel()
        viewModel.validarFormulario()
        viewModel.actualizarNombre("Ana Perez")

        assertNull(viewModel.uiState.value.errores.nombre)
        assertNotNull(viewModel.uiState.value.errores.correo)
        assertNotNull(viewModel.uiState.value.errores.categoria)
        assertNotNull(viewModel.uiState.value.errores.descripcion)
        assertEquals("Ana Perez", viewModel.uiState.value.nombre)
    }

    @Test
    fun corregirTodosLosCamposPermiteContinuarTrasUnIntentoInvalido() {
        val viewModel = ConsultaViewModel()
        assertFalse(viewModel.validarFormulario())

        viewModel.actualizarNombre("Ana Perez")
        viewModel.actualizarCorreo("ana@correo.cl")
        viewModel.actualizarCategoria("Muros")
        viewModel.actualizarDescripcion("Necesito consultar sobre un muro de madera.")

        assertTrue(viewModel.validarFormulario())
    }

    @Test
    fun limpiarConsultaBorraDatosYErrores() {
        val viewModel = consultaCompleta()
        viewModel.actualizarCorreo("incorrecto")
        assertFalse(viewModel.validarFormulario())

        viewModel.limpiarConsulta()
        assertEquals(ConsultaUiState(), viewModel.uiState.value)
    }

    private fun consultaCompleta(): ConsultaViewModel {
        val viewModel = ConsultaViewModel()
        viewModel.actualizarNombre("Ana Perez")
        viewModel.actualizarCorreo("ana@correo.cl")
        viewModel.actualizarCategoria("Muros")
        viewModel.actualizarDescripcion("Necesito consultar sobre un muro de madera.")
        return viewModel
    }
}

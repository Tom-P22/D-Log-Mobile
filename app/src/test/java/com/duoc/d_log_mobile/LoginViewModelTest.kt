package com.duoc.d_log_mobile

import com.duoc.d_log_mobile.viewmodel.LoginViewModel
import org.junit.Assert.*
import org.junit.Test

class LoginViewModelTest {
    @Test
    fun camposVaciosMuestranErrores() {
        val viewModel = LoginViewModel()
        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.uiState.value.errorCorreo)
        assertNotNull(viewModel.uiState.value.errorClave)
    }

    @Test
    fun correoInvalidoNoPermiteEntrar() {
        val viewModel = LoginViewModel()
        viewModel.actualizarCorreo("sin-arroba")
        viewModel.actualizarClave("Demo1234")
        assertFalse(viewModel.validarFormulario())
        assertNotNull(viewModel.uiState.value.errorCorreo)
    }

    @Test
    fun credencialesIncorrectasMuestranMensaje() {
        val viewModel = LoginViewModel()
        viewModel.actualizarCorreo("otra@correo.cl")
        viewModel.actualizarClave("Demo1234")
        assertFalse(viewModel.validarFormulario())
        assertEquals("Correo o contraseña incorrectos", viewModel.uiState.value.mensaje)
    }

    @Test
    fun cuentaDemoEsValida() {
        val viewModel = LoginViewModel()
        viewModel.actualizarCorreo("demo@disenamadera.cl")
        viewModel.actualizarClave("Demo1234")
        assertTrue(viewModel.validarFormulario())
    }

    @Test
    fun editarLimpiaSoloElErrorDelCampo() {
        val viewModel = LoginViewModel()
        viewModel.validarFormulario()
        viewModel.actualizarCorreo("demo@disenamadera.cl")
        assertNull(viewModel.uiState.value.errorCorreo)
        assertNotNull(viewModel.uiState.value.errorClave)
    }
}

package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.d_log_mobile.model.ConsultaErrores
import com.duoc.d_log_mobile.model.ConsultaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConsultaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultaUiState())
    val uiState = _uiState.asStateFlow()

    // Al editar se limpia solo el error de ese campo.
    fun actualizarNombre(valor: String) {
        val estado = _uiState.value
        _uiState.value = estado.copy(nombre = valor, errores = estado.errores.copy(nombre = null))
    }

    fun actualizarCorreo(valor: String) {
        val estado = _uiState.value
        _uiState.value = estado.copy(correo = valor, errores = estado.errores.copy(correo = null))
    }

    fun actualizarCategoria(valor: String) {
        val estado = _uiState.value
        _uiState.value = estado.copy(categoria = valor, errores = estado.errores.copy(categoria = null))
    }

    fun actualizarDescripcion(valor: String) {
        val estado = _uiState.value
        _uiState.value = estado.copy(descripcion = valor, errores = estado.errores.copy(descripcion = null))
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        // Formato basico: texto@dominio.extension, sin espacios ni otro @.
        val formatoCorreo = Regex("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")
        val errores = ConsultaErrores(
            nombre = if (estado.nombre.isBlank()) "Ingresa tu nombre" else null,
            correo = if (estado.correo.isBlank()) {
                "Ingresa tu correo"
            } else if (!formatoCorreo.matches(estado.correo.trim())) {
                "Ingresa un correo válido, por ejemplo nombre@correo.cl"
            } else null,
            categoria = if (estado.categoria.isBlank()) "Ingresa una categoría" else null,
            descripcion = if (estado.descripcion.isBlank()) {
                "Describe tu consulta"
            } else if (estado.descripcion.trim().length < 10) {
                "Escribe al menos 10 caracteres"
            } else null
        )

        _uiState.value = estado.copy(errores = errores)
        val hayErrores = listOfNotNull(
            errores.nombre, errores.correo, errores.categoria, errores.descripcion
        ).isNotEmpty()
        return !hayErrores
    }

    fun limpiarConsulta() {
        _uiState.value = ConsultaUiState()
    }
}

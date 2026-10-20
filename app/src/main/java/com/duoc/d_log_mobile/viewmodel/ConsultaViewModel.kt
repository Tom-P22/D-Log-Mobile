package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.d_log_mobile.model.ConsultaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConsultaViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultaUiState())
    val uiState = _uiState.asStateFlow()

    fun actualizarNombre(valor: String) {
        _uiState.value = _uiState.value.copy(nombre = valor)
    }

    fun actualizarCorreo(valor: String) {
        _uiState.value = _uiState.value.copy(correo = valor)
    }

    fun actualizarCategoria(valor: String) {
        _uiState.value = _uiState.value.copy(categoria = valor)
    }

    fun actualizarDescripcion(valor: String) {
        _uiState.value = _uiState.value.copy(descripcion = valor)
    }

    fun limpiarConsulta() {
        _uiState.value = ConsultaUiState()
    }
}

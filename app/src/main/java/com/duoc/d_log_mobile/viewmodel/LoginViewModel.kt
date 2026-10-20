package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.d_log_mobile.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun actualizarCorreo(correo: String) {
        _uiState.value = _uiState.value.copy(correo = correo)
    }

    fun limpiarCorreo() {
        _uiState.value = LoginUiState()
    }
}

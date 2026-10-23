package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.d_log_mobile.model.LoginUiState
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val preferencias: PreferenciasRepository? = null) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (preferencias != null) {
            viewModelScope.launch {
                preferencias.sesionIniciada.collect { iniciada ->
                    _uiState.update { it.copy(sesionIniciada = iniciada) }
                }
            }
        }
    }

    fun actualizarCorreo(valor: String) {
        _uiState.update { it.copy(correo = valor, errorCorreo = null, mensaje = null) }
    }

    fun actualizarClave(valor: String) {
        _uiState.update { it.copy(clave = valor, errorClave = null, mensaje = null) }
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val formato = Regex("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")
        val errorCorreo = if (estado.correo.isBlank()) {
            "Ingresa tu correo"
        } else if (!formato.matches(estado.correo.trim())) {
            "Ingresa un correo válido"
        } else null
        val errorClave = if (estado.clave.isBlank()) "Ingresa tu contraseña" else null
        _uiState.update { it.copy(errorCorreo = errorCorreo, errorClave = errorClave, mensaje = null) }
        if (errorCorreo != null || errorClave != null) return false

        if (estado.correo.trim() != "demo@disenamadera.cl" || estado.clave != "Demo1234") {
            _uiState.update { it.copy(mensaje = "Correo o contraseña incorrectos") }
            return false
        }
        return true
    }

    fun iniciarSesion() {
        if (_uiState.value.cargando || !validarFormulario()) return
        val repositorio = preferencias ?: return
        _uiState.update { it.copy(cargando = true) }
        viewModelScope.launch {
            try {
                repositorio.guardarSesion(true)
                _uiState.update { it.copy(clave = "", cargando = false) }
            } catch (_: IOException) {
                _uiState.update { it.copy(cargando = false, mensaje = "No se pudo guardar la sesión. Intenta nuevamente.") }
            }
        }
    }

    fun cerrarSesion() {
        if (_uiState.value.cargando) return
        val repositorio = preferencias ?: return
        _uiState.update { it.copy(cargando = true, mensaje = null) }
        viewModelScope.launch {
            try {
                repositorio.guardarSesion(false)
                _uiState.value = LoginUiState(sesionIniciada = false)
            } catch (_: IOException) {
                _uiState.update { it.copy(cargando = false, mensaje = "No se pudo cerrar la sesión. Intenta nuevamente.") }
            }
        }
    }
}

package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.model.SolucionUiState
import com.duoc.d_log_mobile.repository.SolucionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SolucionViewModel : ViewModel() {
    private val repository = SolucionRepository()
    private val _uiState = MutableStateFlow(
        SolucionUiState(soluciones = repository.obtenerSoluciones())
    )
    val uiState = _uiState.asStateFlow()

    // Estado temporal para comprobar la conexión Detalle → Favoritos.
    // La persistencia se implementará mediante un repositorio de preferencias.
    fun alternarFavorito(id: String) {
        val estado = _uiState.value
        if (obtenerSolucion(id) == null) return

        if (id in estado.favoritos) {
            _uiState.value = estado.copy(favoritos = estado.favoritos - id)
        } else {
            _uiState.value = estado.copy(favoritos = estado.favoritos + id)
        }
    }

    fun obtenerSolucion(id: String): Solucion? {
        return _uiState.value.soluciones.find { it.id == id }
    }

    fun limpiarFavoritos() {
        _uiState.value = _uiState.value.copy(favoritos = emptySet())
    }
}

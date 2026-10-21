package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.model.SolucionUiState
import com.duoc.d_log_mobile.repository.SolucionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SolucionViewModel(
    private val repository: SolucionRepository = SolucionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolucionUiState())
    val uiState: StateFlow<SolucionUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(soluciones = repository.obtenerSoluciones()) }
    }

    fun actualizarBusqueda(texto: String) {
        _uiState.update { it.copy(busquedaTexto = texto) }
    }

    fun seleccionarCategoria(categoria: String) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
    }

    fun obtenerSolucion(id: String): Solucion? {
        return repository.obtenerSolucionPorId(id)
    }

    fun alternarFavorito(id: String) {
        _uiState.update { estado ->
            val nuevosFavoritos = estado.favoritos.toMutableSet()
            if (nuevosFavoritos.contains(id)) {
                nuevosFavoritos.remove(id)
            } else {
                nuevosFavoritos.add(id)
            }
            estado.copy(favoritos = nuevosFavoritos)
        }
    }

    fun limpiarFavoritos() {
        _uiState.update { it.copy(favoritos = emptySet()) }
    }
}
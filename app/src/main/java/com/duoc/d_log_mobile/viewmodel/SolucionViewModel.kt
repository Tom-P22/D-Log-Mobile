package com.duoc.d_log_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.model.SolucionUiState
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import com.duoc.d_log_mobile.repository.SolucionRepository
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SolucionViewModel(
    private val repository: SolucionRepository = SolucionRepository(),
    private val preferencias: PreferenciasRepository? = null
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SolucionUiState(soluciones = repository.obtenerSoluciones(), cargando = preferencias != null)
    )
    val uiState = _uiState.asStateFlow()

    init {
        if (preferencias != null) {
            viewModelScope.launch {
                preferencias.favoritos.collect { ids ->
                    _uiState.update { it.copy(favoritos = ids, cargando = false) }
                }
            }
        }
    }

    fun actualizarBusqueda(texto: String) {
        _uiState.update { it.copy(busquedaTexto = texto) }
    }

    fun seleccionarCategoria(categoria: String) {
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
    }

    fun reiniciarFiltros() {
        _uiState.update { it.copy(busquedaTexto = "", categoriaSeleccionada = "Todas", mensaje = null, mensajeSolucionId = null) }
    }

    fun obtenerSolucion(id: String): Solucion? = repository.obtenerSolucionPorId(id)

    fun alternarFavorito(id: String) {
        val estado = _uiState.value
        if (obtenerSolucion(id) == null || estado.cargando || estado.guardandoFavorito) return
        val ids = if (id in estado.favoritos) estado.favoritos - id else estado.favoritos + id
        val mensaje = if (id in ids) "Favorito guardado" else "Favorito eliminado"
        guardarFavoritos(ids, mensaje, id)
    }

    fun limpiarFavoritos() {
        guardarFavoritos(emptySet(), "Favoritos eliminados")
    }

    private fun guardarFavoritos(ids: Set<String>, mensaje: String, solucionId: String? = null) {
        // Sin preferencias, las pruebas locales trabajan solo con el estado en memoria.
        val repositorio = preferencias
        if (repositorio == null) {
            _uiState.update { it.copy(favoritos = ids, mensaje = mensaje, mensajeSolucionId = solucionId) }
            return
        }
        _uiState.update { it.copy(guardandoFavorito = true, mensaje = null, mensajeSolucionId = solucionId) }
        viewModelScope.launch {
            try {
                repositorio.guardarFavoritos(ids)
                _uiState.update { it.copy(guardandoFavorito = false, mensaje = mensaje, mensajeSolucionId = solucionId) }
            } catch (_: IOException) {
                _uiState.update { it.copy(guardandoFavorito = false, mensaje = "No se pudo guardar el cambio. Intenta nuevamente.", mensajeSolucionId = solucionId) }
            }
        }
    }
}

package com.duoc.d_log_mobile.model

data class SolucionUiState(
    val soluciones: List<Solucion> = emptyList(),
    val favoritos: Set<String> = emptySet()
) {
    val solucionesFavoritas: List<Solucion>
        get() = soluciones.filter { it.id in favoritos }
}

package com.duoc.d_log_mobile.model

/**
 * Estado de la interfaz para el catálogo de soluciones, detalle y favoritos.
 */
data class SolucionUiState(
    val soluciones: List<Solucion> = emptyList(),
    val busquedaTexto: String = "",
    val categoriaSeleccionada: String = "Todas",
    val favoritos: Set<String> = emptySet()
) {
    /**
     * Lista filtrada según el texto ingresado y la categoría elegida.
     */
    val solucionesFiltradas: List<Solucion>
        get() = soluciones.filter { solucion ->
            val coincideBusqueda = solucion.nombre.contains(busquedaTexto, ignoreCase = true) ||
                    solucion.descripcion.contains(busquedaTexto, ignoreCase = true)

            val coincideCategoria = categoriaSeleccionada == "Todas" ||
                    solucion.categoria.equals(categoriaSeleccionada, ignoreCase = true)

            coincideBusqueda && coincideCategoria
        }

    val solucionesFavoritas: List<Solucion>
        get() = soluciones.filter { it.id in favoritos }
}
package com.duoc.d_log_mobile.model

/**
 * Ficha técnica que representa una solución constructiva en madera.
 */
data class Solucion(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val sistemaConstructivo: String,
    val material: String,
    val aplicacion: String,
    val recursoRelacionado: String
)
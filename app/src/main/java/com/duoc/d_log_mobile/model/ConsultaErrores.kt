package com.duoc.d_log_mobile.model

// Cada mensaje corresponde a un campo del formulario.
data class ConsultaErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val categoria: String? = null,
    val descripcion: String? = null
)

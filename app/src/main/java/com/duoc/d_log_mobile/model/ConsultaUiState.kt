package com.duoc.d_log_mobile.model

data class ConsultaUiState(
    val nombre: String = "",
    val correo: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val errores: ConsultaErrores = ConsultaErrores()
)

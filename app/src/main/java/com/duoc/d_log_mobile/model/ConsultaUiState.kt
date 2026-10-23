package com.duoc.d_log_mobile.model

import android.net.Uri

data class ConsultaUiState(
    val nombre: String = "",
    val correo: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val imagenUri: Uri? = null,
    val fotoPendiente: Uri? = null,
    val mensajeImagen: String? = null,
    val errores: ConsultaErrores = ConsultaErrores()
)

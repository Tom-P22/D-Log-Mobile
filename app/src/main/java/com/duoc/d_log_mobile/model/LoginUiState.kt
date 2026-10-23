package com.duoc.d_log_mobile.model

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val mensaje: String? = null,
    val cargando: Boolean = false,
    val sesionIniciada: Boolean? = null
)

package com.duoc.d_log_mobile.model

/**
 * @param imagenRes Nombre del drawable sin extensión (ej. "img_muros").
 *   Coloca tus imágenes en res/drawable con ese nombre para personalizar cada ficha.
 */
data class Solucion(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val sistemaConstructivo: String,
    val material: String,
    val aplicacion: String,
    val recursoRelacionado: String,
    val imagenRes: String = "img_default"
)

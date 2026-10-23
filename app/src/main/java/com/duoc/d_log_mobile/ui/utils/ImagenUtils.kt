package com.duoc.d_log_mobile.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import com.duoc.d_log_mobile.R
import java.io.File

fun crearUriFoto(contexto: Context): Uri {
    val carpeta = File(contexto.cacheDir, "imagenes").apply { mkdirs() }
    val archivo = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        contexto,
        "${contexto.packageName}.fileprovider",
        archivo
    )
}

/**
 * Resuelve el id de un drawable por su nombre (sin extensión).
 * Si no existe, usa img_default o fondo_bosque.
 */
fun drawablePorNombre(contexto: Context, nombre: String): Int {
    val id = contexto.resources.getIdentifier(nombre, "drawable", contexto.packageName)
    if (id != 0) return id
    val fallback = contexto.resources.getIdentifier("img_default", "drawable", contexto.packageName)
    return if (fallback != 0) fallback else R.drawable.fondo_bosque
}

/**
 * Carga la imagen de fondo_bosque utilizando muestreo de 4 y desactivando el escalado por densidad.
 */
fun cargarFondoBosque(contexto: Context): Bitmap? {
    val opciones = BitmapFactory.Options().apply {
        inScaled = false
        inSampleSize = 4
    }
    return try {
        BitmapFactory.decodeResource(contexto.resources, R.drawable.fondo_bosque, opciones)
    } catch (_: Exception) {
        null
    }
}



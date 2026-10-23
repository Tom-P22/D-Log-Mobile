package com.duoc.d_log_mobile.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import com.duoc.d_log_mobile.R

fun crearUriFoto(contexto: Context): Uri {
    val carpeta = File(contexto.cacheDir, "imagenes")
    if (!carpeta.exists()) carpeta.mkdirs()
    val archivo = File.createTempFile("consulta_", ".jpg", carpeta)
    return FileProvider.getUriForFile(contexto, "${contexto.packageName}.fileprovider", archivo)
}

fun cargarFondoBosque(contexto: Context): Bitmap? {
    // El original mide 3000 x 4500. Se carga a 750 x 1125, sin escalar por densidad.
    val opciones = BitmapFactory.Options().apply {
        inSampleSize = 4
        inScaled = false
    }
    return BitmapFactory.decodeResource(contexto.resources, R.drawable.fondo_bosque, opciones)
}

fun cargarImagenConsulta(contexto: Context, uri: Uri): Bitmap? {
    val opciones = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    contexto.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, opciones)
    }
    if (opciones.outWidth <= 0 || opciones.outHeight <= 0) return null

    opciones.inSampleSize = 1
    while (opciones.outWidth / opciones.inSampleSize > 1200 ||
        opciones.outHeight / opciones.inSampleSize > 1200) {
        opciones.inSampleSize *= 2
    }
    opciones.inJustDecodeBounds = false
    val imagen = contexto.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, opciones)
    } ?: return null

    // Algunas cámaras guardan la orientación en EXIF sin girar los píxeles.
    // ExifInterface forma parte de Android y está disponible desde nuestra API mínima.
    val orientacion = try {
        contexto.contentResolver.openInputStream(uri)?.use {
            ExifInterface(it).getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )
        } ?: ExifInterface.ORIENTATION_NORMAL
    } catch (_: IOException) {
        ExifInterface.ORIENTATION_NORMAL
    }
    val matriz = Matrix()
    when (orientacion) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matriz.setScale(-1f, 1f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matriz.setRotate(180f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matriz.setScale(1f, -1f)
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matriz.setRotate(90f)
            matriz.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_90 -> matriz.setRotate(90f)
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matriz.setRotate(-90f)
            matriz.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> matriz.setRotate(270f)
    }
    if (matriz.isIdentity) return imagen
    val corregida = Bitmap.createBitmap(imagen, 0, 0, imagen.width, imagen.height, matriz, true)
    if (corregida !== imagen) imagen.recycle()
    return corregida
}

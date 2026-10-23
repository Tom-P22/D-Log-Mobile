package com.duoc.d_log_mobile.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImagenConsulta(uri: Uri?) {
    val contexto = LocalContext.current
    val imagen by produceState<Bitmap?>(initialValue = null, key1 = uri) {
        value = null
        if (uri != null) {
            value = withContext(Dispatchers.IO) {
                try {
                    // Se reduce una foto grande antes de cargarla en memoria.
                    val opciones = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    contexto.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }
                    opciones.inSampleSize = 1
                    while (opciones.outWidth / opciones.inSampleSize > 1200 || opciones.outHeight / opciones.inSampleSize > 1200) {
                        opciones.inSampleSize *= 2
                    }
                    opciones.inJustDecodeBounds = false
                    contexto.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }
                } catch (_: java.io.IOException) {
                    null
                } catch (_: SecurityException) {
                    null
                }
            }
        }
    }
    if (uri != null) {
        val foto = imagen
        if (foto != null) {
            Image(foto.asImageBitmap(), "Imagen de referencia de la consulta", Modifier.fillMaxWidth().height(220.dp), contentScale = ContentScale.Fit)
        } else Text("Cargando imagen o recurso no disponible. Puedes seleccionar otra imagen.")
    }
}

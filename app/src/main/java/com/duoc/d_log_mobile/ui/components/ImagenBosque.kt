package com.duoc.d_log_mobile.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.duoc.d_log_mobile.ui.utils.cargarFondoBosque
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImagenBosque(modifier: Modifier = Modifier, descripcion: String? = null) {
    val contexto = LocalContext.current.applicationContext
    val imagen by produceState<Bitmap?>(initialValue = null, key1 = contexto) {
        value = withContext(Dispatchers.IO) { cargarFondoBosque(contexto) }
    }
    val fondo = imagen
    if (fondo == null) {
        Box(modifier)
    } else {
        Image(fondo.asImageBitmap(), descripcion, modifier, contentScale = ContentScale.Crop)
    }
}

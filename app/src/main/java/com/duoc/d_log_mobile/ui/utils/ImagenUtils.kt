package com.duoc.d_log_mobile.ui.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun crearUriFoto(contexto: Context): Uri {
    val carpeta = File(contexto.cacheDir, "imagenes")
    if (!carpeta.exists()) carpeta.mkdirs()
    val archivo = File.createTempFile("consulta_", ".jpg", carpeta)
    return FileProvider.getUriForFile(contexto, "${contexto.packageName}.fileprovider", archivo)
}

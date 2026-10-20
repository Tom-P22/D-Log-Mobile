package com.duoc.d_log_mobile.navigation

import android.net.Uri

object Routes {
    const val LOGIN = "login"
    const val HOME = "catalogo"
    const val SOLUCION_ID = "solucionId"
    const val DETALLE = "detalle/{$SOLUCION_ID}"
    const val FAVORITOS = "favoritos"
    const val CONSULTA = "consulta"
    const val RESUMEN = "resumen"

    fun detalle(id: String): String = "detalle/${Uri.encode(id)}"
}

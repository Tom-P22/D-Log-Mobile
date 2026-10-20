package com.duoc.d_log_mobile.repository

import com.duoc.d_log_mobile.model.Solucion

class SolucionRepository {
    // Una ficha sintética para verificar navegación por ID. No es el catálogo final.
    fun obtenerSoluciones(): List<Solucion> = listOf(
        Solucion(
            id = "muro-demo",
            nombre = "Tabique de ejemplo",
            descripcion = "Ficha sintética para conectar catálogo y detalle.",
            categoria = "Muros",
            sistemaConstructivo = "Entramado de ejemplo",
            material = "Madera",
            aplicacion = "Referencia académica",
            recursoRelacionado = "Recurso simulado: descripción de montaje de ejemplo."
        )
    )
}

package com.duoc.d_log_mobile.repository

import com.duoc.d_log_mobile.model.Solucion

/**
 * Catálogo sintético para demostración académica.
 * Para personalizar imágenes: reemplaza los PNG en res/drawable
 * (img_muros, img_pisos, img_cubiertas, img_terminaciones, img_default)
 * o cambia el campo imagenRes de cada Solucion al nombre de tu drawable.
 */
class SolucionRepository {

    private val soluciones = listOf(
        Solucion(
            id = "tabique-muro-01",
            nombre = "Tabique Estructural Volcomuro",
            descripcion = "Muro soportante para viviendas de hasta 2 pisos en estructura de madera tratada.",
            categoria = "Muros",
            sistemaConstructivo = "Platform Framing",
            material = "Pino Radiata Grado Estructural",
            aplicacion = "Muros perimetrales y divisiones estructurales interiores.",
            recursoRelacionado = "Ficha Técnica Manual de Construcción en Madera - Sección 4.1",
            imagenRes = "img_muros"
        ),
        Solucion(
            id = "tabique-muro-02",
            nombre = "Muro Cortafuego Termoacústico",
            descripcion = "Muro divisorio de alta resistencia al fuego y aislamiento acústico mejorado.",
            categoria = "Muros",
            sistemaConstructivo = "Entramado Ligero Doble",
            material = "Pino Impregnado CCA + Lana de Vidrio",
            aplicacion = "Muros medianeros entre viviendas adosadas.",
            recursoRelacionado = "Ficha Técnica Resistencia al Fuego RF-60",
            imagenRes = "img_muros"
        ),
        Solucion(
            id = "piso-entrepiso-01",
            nombre = "Entrepiso Estructural Ventilado",
            descripcion = "Sistema de piso rígido con vigas de madera laminada y tablero OSB estructural.",
            categoria = "Pisos",
            sistemaConstructivo = "Vigas de Madera Laminada (MLB)",
            material = "Pino Radiata Laminado + OSB 18mm",
            aplicacion = "Pisos superiores y plataformas de entrepiso en viviendas.",
            recursoRelacionado = "Tabla de Cargas de Entrepiso - Catálogo Técnico",
            imagenRes = "img_pisos"
        ),
        Solucion(
            id = "piso-deck-02",
            nombre = "Deck Exterior de Madera Impregnada",
            descripcion = "Piso exterior resistente a la humedad y exposición intemperie directa.",
            categoria = "Pisos",
            sistemaConstructivo = "Entramado sobre Pilotes",
            material = "Pino Radiata Impregnado CCA Grado Extemperie",
            aplicacion = "Terrazas, pasarelas y bordes de piscina.",
            recursoRelacionado = "Guía de Instalación y Mantenimiento de Decks",
            imagenRes = "img_pisos"
        ),
        Solucion(
            id = "cubierta-cercha-01",
            nombre = "Cercha Tradicional de Cubierta",
            descripcion = "Estructura de techumbre en madera con conectores dentados galvanizados.",
            categoria = "Cubiertas",
            sistemaConstructivo = "Cerchas Conectadas",
            material = "Pino Radiata C16",
            aplicacion = "Estructura soporte para tejas, planchas metálicas o fibrocemento.",
            recursoRelacionado = "Cálculo de Luces y Pendientes de Techumbre",
            imagenRes = "img_cubiertas"
        ),
        Solucion(
            id = "terminacion-revestimiento-01",
            nombre = "Revestimiento Exterior Tinglado",
            descripcion = "Solución estética y protectora de fachada con ventilación trasera integrada.",
            categoria = "Terminaciones",
            sistemaConstructivo = "Fachada Ventilada",
            material = "Pino Termotratado / Tinglado",
            aplicacion = "Revestimiento exterior protector contra lluvias hidrófugas.",
            recursoRelacionado = "Manual de Colocación de Revestimientos Exteriores",
            imagenRes = "img_terminaciones"
        )
    )

    fun obtenerSoluciones(): List<Solucion> = soluciones

    fun obtenerSolucionPorId(id: String): Solucion? {
        return soluciones.find { it.id == id }
    }
}

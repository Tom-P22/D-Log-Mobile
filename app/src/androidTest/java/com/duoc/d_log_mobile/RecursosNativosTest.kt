package com.duoc.d_log_mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.ExifInterface
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import com.duoc.d_log_mobile.ui.utils.crearUriFoto
import com.duoc.d_log_mobile.ui.utils.cargarFondoBosque
import com.duoc.d_log_mobile.ui.utils.cargarImagenConsulta
import com.duoc.d_log_mobile.viewmodel.ConsultaViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class RecursosNativosTest {
    private val contexto = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun fondoSeCargaReducidoSinMultiplicarSuTamanoPorLaDensidad() {
        val fondo = cargarFondoBosque(contexto)!!
        try {
            assertTrue(fondo.width <= 1200)
            assertTrue(fondo.height <= 1200)
            assertTrue(fondo.byteCount <= 6 * 1024 * 1024)
        } finally {
            fondo.recycle()
        }
    }

    @Test
    fun fotografiasRespetanLasOchoOrientacionesExif() {
        // Colores distintos en cada esquina permiten comprobar también los reflejos.
        val coloresEsperados = listOf(
            Color.RED, Color.GREEN, Color.WHITE, Color.BLUE,
            Color.RED, Color.BLUE, Color.WHITE, Color.GREEN
        )
        for (orientacion in 1..8) {
            val uri = crearUriFoto(contexto)
            val original = Bitmap.createBitmap(80, 120, Bitmap.Config.ARGB_8888)
            try {
                val canvas = Canvas(original)
                val pincel = Paint()
                val colores = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.WHITE)
                colores.forEachIndexed { indice, color ->
                    pincel.color = color
                    val izquierda = (indice % 2) * 40f
                    val arriba = (indice / 2) * 60f
                    canvas.drawRect(izquierda, arriba, izquierda + 40f, arriba + 60f, pincel)
                }
                contexto.contentResolver.openOutputStream(uri)!!.use {
                    original.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
                contexto.contentResolver.openFileDescriptor(uri, "rw")!!.use {
                    val exif = ExifInterface(it.fileDescriptor)
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, orientacion.toString())
                    exif.saveAttributes()
                }

                val corregida = cargarImagenConsulta(contexto, uri)!!
                try {
                    assertEquals(if (orientacion >= 5) 120 else 80, corregida.width)
                    assertEquals(if (orientacion >= 5) 80 else 120, corregida.height)
                    val esperado = coloresEsperados[orientacion - 1]
                    val obtenido = corregida.getPixel(10, 10)
                    assertEquals(Color.red(esperado).toDouble(), Color.red(obtenido).toDouble(), 25.0)
                    assertEquals(Color.green(esperado).toDouble(), Color.green(obtenido).toDouble(), 25.0)
                    assertEquals(Color.blue(esperado).toDouble(), Color.blue(obtenido).toDouble(), 25.0)
                } finally {
                    corregida.recycle()
                }
            } finally {
                original.recycle()
                contexto.contentResolver.delete(uri, null, null)
            }
        }
    }

    @Test
    fun preferenciasSeCompartenEntreInstanciasYSalirConservaFavoritos() = runBlocking {
        val primera = PreferenciasRepository(contexto)
        val sesionAnterior = primera.sesionIniciada.first()
        val idsAnteriores = primera.favoritos.first()
        try {
            primera.guardarSesion(true)
            primera.guardarFavoritos(setOf("tabique-muro-01"))
            val segunda = PreferenciasRepository(contexto)
            assertTrue(segunda.sesionIniciada.first())
            assertEquals(setOf("tabique-muro-01"), segunda.favoritos.first())
            segunda.guardarSesion(false)
            assertFalse(primera.sesionIniciada.first())
            assertEquals(setOf("tabique-muro-01"), primera.favoritos.first())
        } finally {
            primera.guardarSesion(sesionAnterior)
            primera.guardarFavoritos(idsAnteriores)
        }
    }

    @Test
    fun uriDeCamaraPermiteEscribirYLeerUnaImagen() {
        val uri = crearUriFoto(contexto)
        assertEquals("content", uri.scheme)
        contexto.contentResolver.openOutputStream(uri)!!.use {
            Bitmap.createBitmap(12, 12, Bitmap.Config.ARGB_8888).compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
        contexto.contentResolver.openInputStream(uri)!!.use {
            assertNotNull(BitmapFactory.decodeStream(it))
        }
        contexto.contentResolver.delete(uri, null, null)
    }

    @Test
    fun cancelarCamaraMantieneLaImagenAnteriorYCapturarLaReemplaza() {
        val viewModel = ConsultaViewModel()
        val anterior = Uri.parse("content://ejemplo/anterior")
        val nueva = crearUriFoto(contexto)
        viewModel.actualizarImagen(anterior)
        viewModel.prepararFoto(nueva)
        viewModel.recibirFoto(false)
        assertEquals(anterior, viewModel.uiState.value.imagenUri)
        assertNull(viewModel.uiState.value.fotoPendiente)
        viewModel.prepararFoto(nueva)
        viewModel.recibirFoto(true)
        assertEquals(nueva, viewModel.uiState.value.imagenUri)
        viewModel.limpiarConsulta()
        assertNull(viewModel.uiState.value.imagenUri)
        contexto.contentResolver.delete(nueva, null, null)
    }
}

package com.duoc.d_log_mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import com.duoc.d_log_mobile.ui.utils.crearUriFoto
import com.duoc.d_log_mobile.viewmodel.ConsultaViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class RecursosNativosTest {
    private val contexto = InstrumentationRegistry.getInstrumentation().targetContext

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

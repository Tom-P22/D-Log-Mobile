package com.duoc.d_log_mobile

import androidx.compose.ui.test.*
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.text.AnnotatedString
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.platform.app.InstrumentationRegistry
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class FlujoAppTest {
    @get:Rule val regla = createAndroidComposeRule<MainActivity>()
    private val contexto = InstrumentationRegistry.getInstrumentation().targetContext
    private val preferencias = PreferenciasRepository(contexto)
    private var sesionAnterior = false
    private var favoritosAnteriores = emptySet<String>()

    @Before
    fun preparar() {
        runBlocking {
            sesionAnterior = preferencias.sesionIniciada.first()
            favoritosAnteriores = preferencias.favoritos.first()
            preferencias.guardarSesion(false)
            preferencias.guardarFavoritos(emptySet())
        }
        regla.waitUntil(10000) { regla.onAllNodesWithTag("loginCorreo").fetchSemanticsNodes().isNotEmpty() }
    }

    @After
    fun restaurar() {
        runBlocking {
            preferencias.guardarSesion(sesionAnterior)
            preferencias.guardarFavoritos(favoritosAnteriores)
        }
    }

    @Test
    fun loginRechazaDatosIncorrectosYPermiteLaCuentaDemo() {
        pulsar("Iniciar sesión")
        regla.onNodeWithText("Ingresa tu correo").assertExists()
        regla.onNodeWithText("Ingresa tu contraseña").assertExists()
        regla.onNodeWithTag("loginCorreo").performTextInput("demo@disenamadera.cl")
        regla.onNodeWithTag("loginClave").performTextInput("incorrecta")
        closeSoftKeyboard()
        pulsar("Iniciar sesión")
        esperar("Correo o contraseña incorrectos")
        regla.onNodeWithTag("loginClave").performTextReplacement("Demo1234")
        closeSoftKeyboard()
        pulsar("Iniciar sesión")
        esperar("Diseña Madera")
        assertTrue(runBlocking { preferencias.sesionIniciada.first() })
        capturar("catalogo")
    }

    @Test
    fun favoritosYSesionSeConservanAlRecrearYSalenCorrectamente() {
        entrar()
        pulsar("Tabique Estructural Volcomuro")
        pulsar("Guardar en favoritos")
        esperar("Quitar de favoritos")
        assertEquals(setOf("tabique-muro-01"), runBlocking { preferencias.favoritos.first() })
        pulsar("Volver")
        regla.activityRule.scenario.recreate()
        esperar("Diseña Madera")
        pulsar("Favoritos (1)")
        esperar("Tabique Estructural Volcomuro")
        capturar("favoritos")
        pulsar("Volver")
        pulsar("Cerrar sesión")
        esperar("Iniciar sesión")
        assertFalse(runBlocking { preferencias.sesionIniciada.first() })
        assertEquals(setOf("tabique-muro-01"), runBlocking { preferencias.favoritos.first() })
        entrar()
        pulsar("Favoritos (1)")
        pulsar("Tabique Estructural Volcomuro")
        pulsar("Quitar de favoritos")
        esperar("Guardar en favoritos")
        assertTrue(runBlocking { preferencias.favoritos.first().isEmpty() })
    }

    @Test
    fun consultaValidaConservaDatosAlEditarYSeLimpiaAlFinalizar() {
        entrar()
        pulsar("Nueva consulta")
        pulsar("Ver resumen")
        regla.onNodeWithText("Ingresa tu nombre").assertExists()
        regla.onNodeWithText("Ingresa tu correo").assertExists()
        capturar("consulta_errores")
        regla.onNodeWithTag("consultaNombre").performScrollTo().performTextInput("Ana Perez")
        regla.onNodeWithTag("consultaCorreo").performScrollTo().performTextInput("ana@correo.cl")
        regla.onNodeWithTag("consultaCategoria").performScrollTo().performTextInput("Muros")
        regla.onNodeWithTag("consultaDescripcion").performScrollTo().performTextInput("Consulta sobre un muro de madera")
        closeSoftKeyboard()
        pulsar("Ver resumen")
        esperar("Resumen de consulta")
        regla.onNodeWithText("Nombre: Ana Perez").assertExists()
        pulsar("Editar consulta")
        regla.onNodeWithTag("consultaNombre").assertTextContains("Ana Perez")
        pulsar("Ver resumen")
        pulsar("Finalizar y volver al catálogo")
        pulsar("Nueva consulta")
        regla.onNodeWithTag("consultaNombre").assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString("")))
        regla.onNodeWithText("Ingresa tu nombre").assertDoesNotExist()
    }

    @Test
    fun cancelarGaleriaYCamaraPermiteSeguirEditando() {
        entrar()
        pulsar("Nueva consulta")
        regla.onNodeWithTag("consultaNombre").performScrollTo().performTextInput("Ana Perez")
        closeSoftKeyboard()
        val dispositivo = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        pulsar("Seleccionar imagen")
        dispositivo.waitForIdle()
        dispositivo.dumpWindowHierarchy(File(contexto.filesDir, "galeria.xml"))
        volverDesdeRecurso(dispositivo)
        pulsar("Tomar fotografía")
        dispositivo.waitForIdle()
        dispositivo.dumpWindowHierarchy(File(contexto.filesDir, "camara.xml"))
        volverDesdeRecurso(dispositivo)
        regla.onNodeWithTag("consultaNombre").assertTextContains("Ana Perez")
        regla.onNodeWithText("No se pudo abrir el recurso. Intenta nuevamente.").assertDoesNotExist()
        capturar("consulta_imagen")
    }

    @Test
    fun fotografiaCapturadaYSeleccionDeGaleriaSeMuestranEnConsulta() {
        entrar()
        pulsar("Nueva consulta")
        val dispositivo = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        pulsar("Tomar fotografía")
        val disparador = dispositivo.wait(Until.findObject(By.res("com.android.camera2", "shutter_button")), 10000)
        assertNotNull("La cámara del emulador debe mostrar el disparador", disparador)
        disparador.click()
        val aceptar = dispositivo.wait(Until.findObject(By.res("com.android.camera2", "done_button")), 10000)
        dispositivo.dumpWindowHierarchy(File(contexto.filesDir, "camara_confirmar.xml"))
        assertNotNull("La cámara debe permitir confirmar la fotografía", aceptar)
        aceptar.click()
        dispositivo.wait(Until.hasObject(By.pkg(contexto.packageName)), 5000)
        esperarImagen()
        capturar("foto_capturada")

        val valores = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "disena_madera_prueba.jpg")
            put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val uri = contexto.contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores)!!
        try {
            contexto.contentResolver.openOutputStream(uri)!!.use {
                android.graphics.Bitmap.createBitmap(80, 80, android.graphics.Bitmap.Config.ARGB_8888).apply {
                    eraseColor(android.graphics.Color.rgb(70, 110, 60))
                }.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, it)
            }
            pulsar("Seleccionar imagen")
            dispositivo.waitForIdle()
            val foto = dispositivo.wait(Until.findObject(By.descContains("Photo taken")), 10000)
            dispositivo.dumpWindowHierarchy(File(contexto.filesDir, "galeria_con_imagen.xml"))
            assertNotNull("El selector debe mostrar la imagen de prueba", foto)
            foto.click()
            dispositivo.wait(Until.hasObject(By.pkg(contexto.packageName)), 5000)
            esperarImagen()
            capturar("foto_galeria")
            pulsar("Quitar imagen")
            regla.onNodeWithContentDescription("Imagen de referencia de la consulta").assertDoesNotExist()
        } finally {
            contexto.contentResolver.delete(uri, null, null)
        }
    }

    @Test
    fun formularioPermiteLlegarALosBotonesEnHorizontal() {
        entrar()
        val dispositivo = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        try {
            dispositivo.setOrientationLeft()
            regla.waitForIdle()
            pulsar("Nueva consulta")
            regla.onNodeWithTag("consultaNombre").performScrollTo().performTextInput("Ana Perez")
            closeSoftKeyboard()
            pulsar("Ver resumen")
            regla.onNodeWithText("Ingresa tu correo").assertExists()
            capturar("consulta_horizontal")
            pulsar("Volver")
            esperar("Diseña Madera")
        } finally {
            dispositivo.setOrientationNatural()
            dispositivo.unfreezeRotation()
        }
    }

    private fun esperarImagen() {
        regla.waitUntil(10000) {
            regla.onAllNodesWithContentDescription("Imagen de referencia de la consulta").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun volverDesdeRecurso(dispositivo: UiDevice) {
        repeat(3) {
            if (dispositivo.currentPackageName != contexto.packageName) {
                dispositivo.pressBack()
                dispositivo.wait(Until.hasObject(By.pkg(contexto.packageName)), 1500)
            }
        }
        assertEquals(contexto.packageName, dispositivo.currentPackageName)
    }

    private fun entrar() {
        regla.onNodeWithTag("loginCorreo").performScrollTo().performTextInput("demo@disenamadera.cl")
        regla.onNodeWithTag("loginClave").performScrollTo().performTextInput("Demo1234")
        closeSoftKeyboard()
        pulsar("Iniciar sesión")
        esperar("Diseña Madera")
    }

    private fun pulsar(texto: String) {
        regla.onNodeWithText(texto).performScrollTo().performClick()
    }

    private fun esperar(texto: String) {
        regla.waitUntil(10000) { regla.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty() }
    }

    private fun capturar(nombre: String) {
        val carpeta = File(contexto.filesDir, "capturas").apply { mkdirs() }
        InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()?.let { bitmap ->
            File(carpeta, "$nombre.png").outputStream().use { bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
        }
    }
}

package com.duoc.d_log_mobile.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.duoc.d_log_mobile.model.ConsultaErrores
import com.duoc.d_log_mobile.model.ConsultaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConsultaViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    private val _uiState = MutableStateFlow(restaurarEstado())
    val uiState = _uiState.asStateFlow()

    private fun restaurarEstado(): ConsultaUiState {
        val imagen = savedStateHandle.get<String>(KEY_IMAGEN)?.let { Uri.parse(it) }
        val pendiente = savedStateHandle.get<String>(KEY_PENDIENTE)?.let { Uri.parse(it) }
        return ConsultaUiState(
            nombre = savedStateHandle.get<String>(KEY_NOMBRE).orEmpty(),
            correo = savedStateHandle.get<String>(KEY_CORREO).orEmpty(),
            categoria = savedStateHandle.get<String>(KEY_CATEGORIA).orEmpty(),
            descripcion = savedStateHandle.get<String>(KEY_DESCRIPCION).orEmpty(),
            imagenUri = imagen,
            fotoPendiente = pendiente
        )
    }

    private fun persistir(estado: ConsultaUiState) {
        savedStateHandle[KEY_NOMBRE] = estado.nombre
        savedStateHandle[KEY_CORREO] = estado.correo
        savedStateHandle[KEY_CATEGORIA] = estado.categoria
        savedStateHandle[KEY_DESCRIPCION] = estado.descripcion
        savedStateHandle[KEY_IMAGEN] = estado.imagenUri?.toString()
        savedStateHandle[KEY_PENDIENTE] = estado.fotoPendiente?.toString()
    }

    // Al editar se limpia solo el error de ese campo.
    fun actualizarNombre(valor: String) {
        val estado = _uiState.value
        val nuevo = estado.copy(nombre = valor, errores = estado.errores.copy(nombre = null))
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun actualizarCorreo(valor: String) {
        val estado = _uiState.value
        val nuevo = estado.copy(correo = valor, errores = estado.errores.copy(correo = null))
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun actualizarCategoria(valor: String) {
        val estado = _uiState.value
        val nuevo = estado.copy(categoria = valor, errores = estado.errores.copy(categoria = null))
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun actualizarDescripcion(valor: String) {
        val estado = _uiState.value
        val nuevo = estado.copy(descripcion = valor, errores = estado.errores.copy(descripcion = null))
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        // Formato basico: texto@dominio.extension, sin espacios ni otro @.
        val formatoCorreo = Regex("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")
        val errores = ConsultaErrores(
            nombre = if (estado.nombre.isBlank()) "Ingresa tu nombre" else null,
            correo = if (estado.correo.isBlank()) {
                "Ingresa tu correo"
            } else if (!formatoCorreo.matches(estado.correo.trim())) {
                "Ingresa un correo válido, por ejemplo nombre@correo.cl"
            } else null,
            categoria = if (estado.categoria.isBlank()) "Ingresa una categoría" else null,
            descripcion = if (estado.descripcion.isBlank()) {
                "Describe tu consulta"
            } else if (estado.descripcion.trim().length < 10) {
                "Escribe al menos 10 caracteres"
            } else null
        )

        _uiState.value = estado.copy(errores = errores)
        val hayErrores = listOfNotNull(
            errores.nombre, errores.correo, errores.categoria, errores.descripcion
        ).isNotEmpty()
        return !hayErrores
    }

    fun prepararFoto(uri: Uri) {
        val nuevo = _uiState.value.copy(fotoPendiente = uri, mensajeImagen = null)
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun recibirFoto(guardada: Boolean) {
        val estado = _uiState.value
        val nuevo = estado.copy(
            imagenUri = if (guardada) estado.fotoPendiente ?: estado.imagenUri else estado.imagenUri,
            fotoPendiente = null
        )
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun actualizarImagen(uri: Uri?) {
        val nuevo = _uiState.value.copy(imagenUri = uri, mensajeImagen = null)
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun mostrarErrorImagen() {
        val nuevo = _uiState.value.copy(
            fotoPendiente = null,
            mensajeImagen = "No se pudo abrir el recurso. Intenta nuevamente."
        )
        _uiState.value = nuevo
        persistir(nuevo)
    }

    fun limpiarConsulta() {
        _uiState.value = ConsultaUiState()
        persistir(ConsultaUiState())
    }

    companion object {
        private const val KEY_NOMBRE = "consulta_nombre"
        private const val KEY_CORREO = "consulta_correo"
        private const val KEY_CATEGORIA = "consulta_categoria"
        private const val KEY_DESCRIPCION = "consulta_descripcion"
        private const val KEY_IMAGEN = "consulta_imagen"
        private const val KEY_PENDIENTE = "consulta_pendiente"
    }
}

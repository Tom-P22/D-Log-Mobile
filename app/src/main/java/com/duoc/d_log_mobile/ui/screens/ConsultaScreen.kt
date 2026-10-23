package com.duoc.d_log_mobile.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import com.duoc.d_log_mobile.ui.theme.GlacialIndifference
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.model.ConsultaErrores
import com.duoc.d_log_mobile.model.ConsultaUiState
import com.duoc.d_log_mobile.ui.components.ImagenConsulta
import com.duoc.d_log_mobile.ui.components.PantallaBase
import com.duoc.d_log_mobile.ui.theme.DLogMobileTheme
import com.duoc.d_log_mobile.ui.utils.crearUriFoto

private val CATEGORIAS_CONSULTA = listOf(
    "Muros",
    "Pisos",
    "Cubiertas",
    "Terminaciones",
    "Otra"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultaScreen(
    state: ConsultaUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onResumen: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    onImagenChange: (Uri?) -> Unit = {},
    onPrepararFoto: (Uri) -> Unit = {},
    onRecibirFoto: (Boolean) -> Unit = {},
    onErrorImagen: () -> Unit = {}
) {
    val contexto = LocalContext.current
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) onImagenChange(uri)
    }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { guardada ->
        onRecibirFoto(guardada)
    }

    // Dropdown: si la categoría actual no está en la lista fija, se trata como "Otra"
    val esOtra = state.categoria.isNotBlank() && state.categoria !in CATEGORIAS_CONSULTA.filter { it != "Otra" }
    var expandido by remember { mutableStateOf(false) }
    var seleccionDropdown by remember(state.categoria) {
        mutableStateOf(
            when {
                state.categoria.isBlank() -> ""
                state.categoria in CATEGORIAS_CONSULTA && state.categoria != "Otra" -> state.categoria
                else -> "Otra"
            }
        )
    }
    var textoOtra by remember(state.categoria) {
        mutableStateOf(if (esOtra) state.categoria else "")
    }

    PantallaBase(
        titulo = "Consulta técnica",
        subtitulo = "Completa los campos. No se envía información real.",
        onVolver = onVolver,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = state.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            singleLine = true,
            isError = state.errores.nombre != null,
            supportingText = { state.errores.nombre?.let { Text(it) } },
            trailingIcon = { if (state.errores.nombre != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth().testTag("consultaNombre"),
            shape = RoundedCornerShape(16.dp)
        )
        OutlinedTextField(
            value = state.correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.errores.correo != null,
            supportingText = {
                Text(
                    state.errores.correo
                        ?: "Ejemplo: nombre@correo.cl"
                )
            },
            trailingIcon = { if (state.errores.correo != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth().testTag("consultaCorreo"),
            shape = RoundedCornerShape(16.dp)
        )

        // Menú desplegable de categoría (colores del tema + Glacial Indifference)
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = seleccionDropdown.ifBlank { "Selecciona una categoría" },
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        "Categoría de solución",
                        fontFamily = GlacialIndifference
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = GlacialIndifference
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                },
                isError = state.errores.categoria != null,
                supportingText = {
                    state.errores.categoria?.let {
                        Text(it, fontFamily = GlacialIndifference)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("consultaCategoria"),
                shape = RoundedCornerShape(16.dp)
            )
            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                CATEGORIAS_CONSULTA.forEach { opcion ->
                    val seleccionada = seleccionDropdown == opcion
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = opcion,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = GlacialIndifference
                                ),
                                color = if (seleccionada) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        },
                        onClick = {
                            seleccionDropdown = opcion
                            expandido = false
                            if (opcion == "Otra") {
                                onCategoriaChange(textoOtra)
                            } else {
                                textoOtra = ""
                                onCategoriaChange(opcion)
                            }
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                            leadingIconColor = MaterialTheme.colorScheme.onSurface,
                            trailingIconColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        AnimatedVisibility(visible = seleccionDropdown == "Otra") {
            OutlinedTextField(
                value = textoOtra,
                onValueChange = {
                    textoOtra = it
                    onCategoriaChange(it)
                },
                label = { Text("Especifica la categoría") },
                singleLine = true,
                isError = state.errores.categoria != null && textoOtra.isBlank(),
                supportingText = {
                    if (state.errores.categoria != null && textoOtra.isBlank()) {
                        Text(state.errores.categoria!!)
                    } else {
                        Text("Escribe la categoría personalizada")
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("consultaCategoriaOtra"),
                shape = RoundedCornerShape(16.dp)
            )
        }

        OutlinedTextField(
            value = state.descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción de la consulta") },
            minLines = 3,
            isError = state.errores.descripcion != null,
            supportingText = {
                Text(state.errores.descripcion ?: "Escribe al menos 10 caracteres")
            },
            trailingIcon = { if (state.errores.descripcion != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth().testTag("consultaDescripcion"),
            shape = RoundedCornerShape(16.dp)
        )

        Text("Imagen de referencia (opcional)", style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    try {
                        galeria.launch("image/*")
                    } catch (_: Exception) {
                        onErrorImagen()
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Outlined.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(6.dp))
                Text("Galería")
            }
            OutlinedButton(
                onClick = {
                    try {
                        val uri = crearUriFoto(contexto)
                        onPrepararFoto(uri)
                        camara.launch(uri)
                    } catch (_: Exception) {
                        onErrorImagen()
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Outlined.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(6.dp))
                Text("Cámara")
            }
        }
        AnimatedVisibility(state.mensajeImagen != null) {
            Text(
                state.mensajeImagen.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        ImagenConsulta(state.imagenUri)
        if (state.imagenUri != null) {
            TextButton(onClick = { onImagenChange(null) }) { Text("Quitar imagen") }
        }

        Spacer(Modifier.height(4.dp))
        Button(
            onClick = onResumen,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Ver resumen")
        }
    }
}

@Composable
private fun IconoErrorConsulta() {
    Icon(
        painter = painterResource(R.drawable.ic_error),
        contentDescription = "Error en el campo",
        tint = MaterialTheme.colorScheme.error
    )
}

@Preview(showBackground = true)
@Composable
fun ConsultaScreenPreview() {
    DLogMobileTheme {
        ConsultaScreen(ConsultaUiState(), {}, {}, {}, {}, {}, {})
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ConsultaScreenErroresPreview() {
    DLogMobileTheme {
        ConsultaScreen(
            state = ConsultaUiState(
                errores = ConsultaErrores(
                    nombre = "Ingresa tu nombre",
                    correo = "Ingresa un correo válido, por ejemplo nombre@correo.cl",
                    categoria = "Ingresa una categoría",
                    descripcion = "Escribe al menos 10 caracteres"
                )
            ),
            onNombreChange = {},
            onCorreoChange = {},
            onCategoriaChange = {},
            onDescripcionChange = {},
            onResumen = {},
            onVolver = {}
        )
    }
}

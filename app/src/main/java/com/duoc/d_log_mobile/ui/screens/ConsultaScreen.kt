package com.duoc.d_log_mobile.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.model.ConsultaErrores
import com.duoc.d_log_mobile.model.ConsultaUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase
import com.duoc.d_log_mobile.ui.theme.DLogMobileTheme

@Composable
fun ConsultaScreen(
    state: ConsultaUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onResumen: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaBase("Consulta técnica", modifier) {
        Text("Completa todos los campos para revisar tu consulta. No se envía información.")
        OutlinedTextField(
            value = state.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            singleLine = true,
            isError = state.errores.nombre != null,
            supportingText = { state.errores.nombre?.let { Text(it) } },
            trailingIcon = { if (state.errores.nombre != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = state.errores.correo != null,
            supportingText = { state.errores.correo?.let { Text(it) } },
            trailingIcon = { if (state.errores.correo != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.categoria,
            onValueChange = onCategoriaChange,
            label = { Text("Categoría de solución") },
            singleLine = true,
            isError = state.errores.categoria != null,
            supportingText = { Text(state.errores.categoria ?: "Ej.: Muros, Pisos, Cubiertas o Terminaciones") },
            trailingIcon = { if (state.errores.categoria != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción de la consulta") },
            minLines = 3,
            isError = state.errores.descripcion != null,
            supportingText = { Text(state.errores.descripcion ?: "Escribe al menos 10 caracteres") },
            trailingIcon = { if (state.errores.descripcion != null) IconoErrorConsulta() },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onResumen) { Text("Ver resumen") }
        Button(onClick = onVolver) { Text("Volver") }
    }
}

// El mismo icono se reutiliza en los cuatro campos, sin librerias adicionales.
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

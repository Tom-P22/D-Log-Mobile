package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duoc.d_log_mobile.model.LoginUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun LoginScreen(
    state: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onAbrirRecorrido: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaBase("Diseña Madera", modifier) {
        Text("Base de navegación. El inicio de sesión aún no está implementado.")
        OutlinedTextField(
            value = state.correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo de prueba") },
            singleLine = true
        )
        Button(onClick = onAbrirRecorrido) { Text("Abrir catálogo de prueba") }
    }
}

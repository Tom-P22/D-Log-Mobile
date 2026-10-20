package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duoc.d_log_mobile.model.ConsultaUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase

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
    PantallaBase("Consulta", modifier) {
        Text("Formulario base sin validación ni fotografía. No se envía información.")
        OutlinedTextField(state.nombre, onNombreChange, label = { Text("Nombre") }, singleLine = true)
        OutlinedTextField(state.correo, onCorreoChange, label = { Text("Correo") }, singleLine = true)
        OutlinedTextField(state.categoria, onCategoriaChange, label = { Text("Categoría de prueba") }, singleLine = true)
        OutlinedTextField(state.descripcion, onDescripcionChange, label = { Text("Descripción") })
        Button(onClick = onResumen) { Text("Ver resumen de prueba") }
        Button(onClick = onVolver) { Text("Volver") }
    }
}

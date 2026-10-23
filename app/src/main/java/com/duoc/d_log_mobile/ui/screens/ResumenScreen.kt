package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duoc.d_log_mobile.model.ConsultaUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase
import com.duoc.d_log_mobile.ui.components.ImagenConsulta

@Composable
fun ResumenScreen(
    state: ConsultaUiState,
    onEditar: () -> Unit,
    onFinalizar: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaBase("Resumen de consulta", modifier) {
        Text("Nombre: ${state.nombre}")
        Text("Correo: ${state.correo}")
        Text("Categoría: ${state.categoria}")
        Text("Descripción: ${state.descripcion}")
        ImagenConsulta(state.imagenUri)
        Text("Información temporal; no se ha enviado una consulta.")
        Button(onClick = onEditar) { Text("Editar consulta") }
        Button(onClick = onFinalizar) { Text("Finalizar y volver al catálogo") }
    }
}

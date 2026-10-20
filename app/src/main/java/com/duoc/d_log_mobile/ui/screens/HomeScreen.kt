package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.duoc.d_log_mobile.ui.theme.DLogMobileTheme
import com.duoc.d_log_mobile.model.SolucionUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun HomeScreen(
    state: SolucionUiState,
    onDetalle: (String) -> Unit,
    onFavoritos: () -> Unit,
    onConsulta: () -> Unit,
    onSalir: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaBase("Catálogo", modifier) {
        Text("Datos de prueba. La búsqueda y los filtros se agregarán en su tarea.")
        if (state.soluciones.isEmpty()) Text("No hay soluciones disponibles.")
        state.soluciones.forEach { solucion ->
            Button(onClick = { onDetalle(solucion.id) }) { Text(solucion.nombre) }
        }
        Button(onClick = onFavoritos) { Text("Favoritos") }
        Button(onClick = onConsulta) { Text("Nueva consulta") }
        Button(onClick = onSalir) { Text("Salir del recorrido de prueba") }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DLogMobileTheme {
        HomeScreen(SolucionUiState(), {}, {}, {}, {})
    }
}

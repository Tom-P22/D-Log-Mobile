package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun FavoritosScreen(
    soluciones: List<Solucion>,
    onDetalle: (String) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    cargando: Boolean = false
) {
    PantallaBase("Favoritos", modifier) {
        Text("Tus favoritos se conservan al cerrar la app.")
        if (cargando) CircularProgressIndicator()
        if (!cargando && soluciones.isEmpty()) Text("Aún no hay favoritos.")
        soluciones.forEach { solucion ->
            Button(onClick = { onDetalle(solucion.id) }) { Text(solucion.nombre) }
        }
        Button(onClick = onVolver) { Text("Volver") }
    }
}

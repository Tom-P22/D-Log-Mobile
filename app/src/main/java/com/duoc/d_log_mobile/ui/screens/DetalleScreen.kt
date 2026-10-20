package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun DetalleScreen(
    solucion: Solucion?,
    esFavorita: Boolean,
    onFavorito: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaBase("Detalle de solución", modifier) {
        if (solucion == null) {
            Text("No se encontró la solución.")
        } else {
            Text(solucion.nombre)
            Text(solucion.descripcion)
            Text("Categoría: ${solucion.categoria}")
            Text("Sistema: ${solucion.sistemaConstructivo}")
            Text("Material: ${solucion.material}")
            Text("Uso: ${solucion.aplicacion}")
            Text(solucion.recursoRelacionado)
            Button(onClick = onFavorito) {
                Text(if (esFavorita) "Quitar favorito temporal" else "Agregar favorito temporal")
            }
        }
        Button(onClick = onVolver) { Text("Volver") }
    }
}

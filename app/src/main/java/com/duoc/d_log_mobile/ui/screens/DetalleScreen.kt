package com.duoc.d_log_mobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import com.duoc.d_log_mobile.ui.components.ImagenBosque
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun DetalleScreen(
    solucion: Solucion?,
    esFavorita: Boolean,
    onVolver: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier,
    guardando: Boolean = false,
    mensaje: String? = null
) {
    val colorFavorito by animateColorAsState(
        if (esFavorita) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "colorFavorito"
    )
    PantallaBase(titulo = "Detalle Técnico", modifier = modifier) {
        Text("Ficha sintetica de ejemplo. Sin prestaciones tecnicas certificadas.")
        if (solucion == null) {
            Text(
                text = "No se encontró la solución seleccionada.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onVolver) {
                Text("Volver al catálogo")
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ImagenBosque(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        descripcion = "Imagen de referencia de madera y naturaleza"
                    )
                    Text(
                        text = solucion.nombre,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    SeccionDetalle(label = "Categoría", valor = solucion.categoria)
                    SeccionDetalle(label = "Sistema Constructivo", valor = solucion.sistemaConstructivo)
                    SeccionDetalle(label = "Material Predominante", valor = solucion.material)
                    SeccionDetalle(label = "Aplicación Principal", valor = solucion.aplicacion)
                    SeccionDetalle(label = "Descripción", valor = solucion.descripcion)
                    SeccionDetalle(label = "Recurso / Norma", valor = solucion.recursoRelacionado)
                }
            }

            if (guardando) CircularProgressIndicator()
            AnimatedVisibility(mensaje != null) { Text(mensaje.orEmpty()) }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onFavorito,
                    enabled = !guardando,
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = colorFavorito),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (esFavorita) "Quitar de favoritos" else "Guardar en favoritos")
                }

                Button(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

@Composable
private fun SeccionDetalle(label: String, valor: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

package com.duoc.d_log_mobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.ui.components.PantallaBase
import com.duoc.d_log_mobile.ui.utils.drawablePorNombre
import androidx.compose.ui.platform.LocalContext

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
        if (esFavorita) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "colorFavorito"
    )

    PantallaBase(
        titulo = "Detalle Técnico",
        subtitulo = "Ficha de ejemplo — sin prestaciones certificadas",
        onVolver = onVolver,
        modifier = modifier
    ) {
        if (solucion == null) {
            Text(
                text = "No se encontró la solución seleccionada.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Volver al catálogo")
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val contexto = LocalContext.current
                    val imagenId = drawablePorNombre(contexto, solucion.imagenRes)
                    Image(
                        painter = painterResource(imagenId),
                        contentDescription = solucion.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
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

            if (guardando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            AnimatedVisibility(mensaje != null) {
                Text(mensaje.orEmpty(), style = MaterialTheme.typography.bodyMedium)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onFavorito,
                    enabled = !guardando,
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = colorFavorito),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        if (esFavorita) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(if (esFavorita) "Quitar de favoritos" else "Guardar en favoritos")
                }
                Button(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
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

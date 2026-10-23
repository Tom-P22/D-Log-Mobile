package com.duoc.d_log_mobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.model.SolucionUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase
import com.duoc.d_log_mobile.ui.utils.drawablePorNombre
import com.duoc.d_log_mobile.ui.utils.esPantallaAncha

@Composable
fun HomeScreen(
    state: SolucionUiState,
    onBusquedaChange: (String) -> Unit,
    onCategoriaSelect: (String) -> Unit,
    onDetalle: (String) -> Unit,
    onFavoritos: () -> Unit,
    onConsulta: () -> Unit,
    onSalir: () -> Unit,
    modifier: Modifier = Modifier,
    saliendo: Boolean = false,
    mensajeSesion: String? = null
) {
    val categorias = listOf("Todas", "Muros", "Pisos", "Cubiertas", "Terminaciones")
    val ancho = esPantallaAncha()

    PantallaBase(
        titulo = "Diseña Madera",
        subtitulo = "Catálogo de soluciones constructivas",
        modifier = modifier
    ) {
        if (ancho) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onFavoritos,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Favoritos (${state.favoritos.size})")
                }
                Button(
                    onClick = onConsulta,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Nueva consulta")
                }
            }
        } else {
            OutlinedButton(
                onClick = onFavoritos,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(8.dp))
                Text("Favoritos (${state.favoritos.size})")
            }
            Button(
                onClick = onConsulta,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.size(8.dp))
                Text("Nueva consulta")
            }
        }

        if (state.cargando) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        AnimatedVisibility(mensajeSesion != null) {
            Text(
                mensajeSesion.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedTextField(
            value = state.busquedaTexto,
            onValueChange = onBusquedaChange,
            label = { Text("Buscar solución constructiva...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        Text(
            text = "Categorías",
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categorias) { cat ->
                FilterChip(
                    selected = state.categoriaSeleccionada.equals(cat, ignoreCase = true),
                    onClick = { onCategoriaSelect(cat) },
                    label = { Text(cat) },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        if (state.solucionesFiltradas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No se encontraron soluciones",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Prueba con otra búsqueda o categoría",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                text = "${state.solucionesFiltradas.size} resultado(s)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.solucionesFiltradas.forEach { solucion ->
                    TarjetaSolucion(
                        solucion = solucion,
                        onDetalle = { onDetalle(solucion.id) }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        TextButton(
            onClick = onSalir,
            enabled = !saliendo,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                Icons.Outlined.Logout,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.size(6.dp))
            Text(if (saliendo) "Cerrando sesión…" else "Cerrar sesión")
        }
    }
}

@Composable
private fun TarjetaSolucion(
    solucion: Solucion,
    onDetalle: () -> Unit
) {
    val contexto = LocalContext.current
    val imagenId = drawablePorNombre(contexto, solucion.imagenRes)
    Card(
        onClick = onDetalle,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Image(
                painter = painterResource(imagenId),
                contentDescription = solucion.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = solucion.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Categoría: ${solucion.categoria}  ·  Material: ${solucion.material}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = solucion.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

package com.duoc.d_log_mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.model.Solucion
import com.duoc.d_log_mobile.model.SolucionUiState
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun HomeScreen(
    state: SolucionUiState,
    onBusquedaChange: (String) -> Unit,
    onCategoriaSelect: (String) -> Unit,
    onDetalle: (String) -> Unit,
    onFavoritos: () -> Unit,
    onNuevaConsulta: () -> Unit,
    onSalir: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categorias = listOf("Todas", "Muros", "Pisos", "Cubiertas", "Terminaciones")

    PantallaBase(titulo = "Diseña Madera", modifier = modifier) {
        // Barra de acciones superiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onFavoritos) {
                Text("Favoritos (${state.favoritos.size})")
            }
            Button(onClick = onNuevaConsulta) {
                Text("Nueva consulta")
            }
        }

        // Campo de búsqueda
        OutlinedTextField(
            value = state.busquedaTexto,
            onValueChange = onBusquedaChange,
            label = { Text("Buscar solución constructiva...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Filtros por Categoría
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { cat ->
                FilterChip(
                    selected = state.categoriaSeleccionada.equals(cat, ignoreCase = true),
                    onClick = { onCategoriaSelect(cat) },
                    label = { Text(cat) }
                )
            }
        }

        // Listado de Soluciones o Estado Vacío
        if (state.solucionesFiltradas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No se encontraron soluciones",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Prueba con otra búsqueda o categoría",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.solucionesFiltradas.forEach { solucion ->
                    TarjetaSolucion(
                        solucion = solucion,
                        onDetalle = { onDetalle(solucion.id) }
                    )
                }
            }
        }

        TextButton(
            onClick = onSalir,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun TarjetaSolucion(
    solucion: Solucion,
    onDetalle: () -> Unit
) {
    Card(
        onClick = onDetalle,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = solucion.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Categoría: ${solucion.categoria} | Material: ${solucion.material}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = solucion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
        }
    }
}
package com.duoc.d_log_mobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.model.ConsultaUiState
import com.duoc.d_log_mobile.ui.components.ImagenConsulta
import com.duoc.d_log_mobile.ui.components.PantallaBase

@Composable
fun ResumenScreen(
    state: ConsultaUiState,
    onEditar: () -> Unit,
    onFinalizar: () -> Unit,
    modifier: Modifier = Modifier
) {
    PantallaBase(
        titulo = "Resumen de consulta",
        subtitulo = "Revisa los datos antes de finalizar (no se envía nada)",
        onVolver = onEditar,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilaResumen("Nombre", state.nombre)
                FilaResumen("Correo", state.correo)
                FilaResumen("Categoría", state.categoria)
                FilaResumen("Descripción", state.descripcion)
            }
        }
        ImagenConsulta(state.imagenUri)
        Text(
            "Información temporal; no se ha enviado una consulta.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        OutlinedButton(
            onClick = onEditar,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Editar consulta")
        }
        Button(
            onClick = onFinalizar,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Finalizar y volver al catálogo")
        }
    }
}

@Composable
private fun FilaResumen(label: String, valor: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = valor.ifBlank { "—" },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

package com.duoc.d_log_mobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.ui.utils.esPantallaAncha

/**
 * Contenedor base con el mismo lenguaje visual del login:
 * fondo de bosque + tarjeta semitransparente con esquinas redondeadas.
 */
@Composable
fun PantallaBase(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    onVolver: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val ancho = esPantallaAncha()
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo_bosque),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(if (ancho) 24.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(if (ancho) 24.dp else 12.dp))
            Surface(
                modifier = Modifier
                    .widthIn(max = if (ancho) 840.dp else 600.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                contentColor = MaterialTheme.colorScheme.onSurface,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(if (ancho) 28.dp else 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (onVolver != null) {
                            IconButton(
                                onClick = onVolver,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = titulo,
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = if (onVolver != null) TextAlign.Start else TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (subtitulo != null) {
                                Text(
                                    text = subtitulo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = if (onVolver != null) TextAlign.Start else TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    content()
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

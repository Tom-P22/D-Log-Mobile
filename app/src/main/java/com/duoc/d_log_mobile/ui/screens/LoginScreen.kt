package com.duoc.d_log_mobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.model.LoginUiState
import com.duoc.d_log_mobile.ui.utils.esPantallaAncha

@Composable
fun LoginScreen(
    state: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarClave by rememberSaveable { mutableStateOf(false) }
    val ancho = esPantallaAncha()
    Box(modifier.fillMaxSize()) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = if (ancho) 520.dp else 440.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                contentColor = MaterialTheme.colorScheme.onSurface,
                shadowElevation = 8.dp
            ) {
                Column(
                    Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo circular
                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 2.dp
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_dlog),
                            contentDescription = "Logo D-Log",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .padding(8.dp)
                        )
                    }
                    Text(
                        "D-Log",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Soluciones constructivas en madera",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.correo,
                        onValueChange = onCorreoChange,
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        enabled = !state.cargando,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        isError = state.errorCorreo != null,
                        supportingText = { state.errorCorreo?.let { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("loginCorreo"),
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = state.clave,
                        onValueChange = onClaveChange,
                        label = { Text("Contraseña") },
                        singleLine = true,
                        enabled = !state.cargando,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (mostrarClave) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { mostrarClave = !mostrarClave }) {
                                Icon(
                                    if (mostrarClave) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (mostrarClave) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    }
                                )
                            }
                        },
                        isError = state.errorClave != null,
                        supportingText = { state.errorClave?.let { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("loginClave"),
                        shape = RoundedCornerShape(16.dp)
                    )
                    AnimatedVisibility(state.mensaje != null) {
                        Text(
                            state.mensaje.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Button(
                        onClick = onIniciarSesion,
                        enabled = !state.cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (state.cargando) {
                            CircularProgressIndicator(
                                Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Iniciar sesión")
                        }
                    }
                    Text(
                        "Cuenta de prueba: demo@disenamadera.cl\nContraseña: Demo1234",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

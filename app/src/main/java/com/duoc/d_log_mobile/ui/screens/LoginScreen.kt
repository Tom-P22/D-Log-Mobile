package com.duoc.d_log_mobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.model.LoginUiState
import com.duoc.d_log_mobile.ui.utils.esPantallaAncha

private val fuenteLogin = FontFamily(
    Font(R.font.glacial_indifference_regular, FontWeight.Normal),
    Font(R.font.glacial_indifference_bold, FontWeight.Bold)
)

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
            painterResource(R.drawable.fondo_bosque), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.widthIn(max = if (ancho) 520.dp else 440.dp).fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Column(
                    Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("D-Log", style = MaterialTheme.typography.headlineLarge, fontFamily = fuenteLogin)
                    Text("Soluciones constructivas en madera", textAlign = TextAlign.Center, fontFamily = fuenteLogin)
                    OutlinedTextField(
                        value = state.correo, onValueChange = onCorreoChange,
                        label = { Text("Correo electrónico") }, singleLine = true,
                        enabled = !state.cargando,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        isError = state.errorCorreo != null,
                        supportingText = { state.errorCorreo?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth().testTag("loginCorreo")
                    )
                    OutlinedTextField(
                        value = state.clave, onValueChange = onClaveChange,
                        label = { Text("Contraseña") }, singleLine = true,
                        enabled = !state.cargando,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (mostrarClave) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { mostrarClave = !mostrarClave }) {
                                Icon(
                                    if (mostrarClave) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (mostrarClave) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        isError = state.errorClave != null,
                        supportingText = { state.errorClave?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth().testTag("loginClave")
                    )
                    AnimatedVisibility(state.mensaje != null) {
                        Text(state.mensaje.orEmpty(), color = MaterialTheme.colorScheme.error)
                    }
                    Button(onClick = onIniciarSesion, enabled = !state.cargando, modifier = Modifier.fillMaxWidth()) {
                        if (state.cargando) {
                            CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else Text("Iniciar sesión")
                    }
                    Text("Cuenta de prueba: demo@disenamadera.cl\nContraseña: Demo1234", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

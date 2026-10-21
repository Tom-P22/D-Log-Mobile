package com.duoc.d_log_mobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.model.LoginUiState

// ==========================================
// TIPOGRAFÍA
// ==========================================
val GlacialIndifference = FontFamily(
    Font(R.font.glacial_indifference_regular, FontWeight.Normal),
    Font(R.font.glacial_indifference_bold, FontWeight.Bold)
)

// ==========================================
// PALETA DE COLORES
// ==========================================
val TranslucentGreenCard = Color(0xD9C2CEA9) // Tarjeta verde translúcida
val InputBeige           = Color(0xFFE4CFB0) // Fondo crema/arena para las cajas
val ForestDarkGreen      = Color(0xFF1B3C31) // Verde oscuro profundo (Textos y botón)
val DarkText             = Color(0xFF2C3227) // Texto secundario
val LightButtonText      = Color(0xFFC0CEA7) // Texto claro dentro del botón

@Composable
fun LoginScreen(
    state: LoginUiState,
    onCorreoChange: (String) -> Unit,
    onAbrirRecorrido: () -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // --- 1. IMAGEN DE FONDO A PANTALLA COMPLETA ---
        Image(
            painter = painterResource(id = R.drawable.fondo_bosque),
            contentDescription = "Fondo de bosque",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // --- 2. TARJETA CENTRADA TRANSLÚCIDA ---
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(28.dp),
            color = TranslucentGreenCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- TÍTULO PRINCIPAL ---
                Text(
                    text = "D-Log",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = GlacialIndifference,
                    color = ForestDarkGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                // --- SUBTÍTULO ---
                Text(
                    text = "Diseño y carpintería paso a paso",
                    fontSize = 20.sp,
                    fontFamily = GlacialIndifference,
                    color = DarkText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- CAMPO: CORREO ELECTRÓNICO ---
                TextField(
                    value = state.correo,
                    onValueChange = onCorreoChange,
                    placeholder = {
                        Text("Correo electrónico", fontFamily = GlacialIndifference, color = DarkText)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = ForestDarkGreen
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputBeige,
                        unfocusedContainerColor = InputBeige,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = ForestDarkGreen,
                        unfocusedTextColor = ForestDarkGreen
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- CAMPO: CONTRASEÑA ---
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text("Contraseña", fontFamily = GlacialIndifference, color = DarkText)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ForestDarkGreen
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = DarkText
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputBeige,
                        unfocusedContainerColor = InputBeige,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = ForestDarkGreen,
                        unfocusedTextColor = ForestDarkGreen
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // --- ¿OLVIDASTE TU CONTRASEÑA? ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = DarkText,
                            fontSize = 12.sp,
                            fontFamily = GlacialIndifference
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- BOTÓN INICIAR SESIÓN ---
                Button(
                    onClick = onAbrirRecorrido,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestDarkGreen,
                        contentColor = LightButtonText
                    )
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = GlacialIndifference,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- FOOTER: REGÍSTRATE ---
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Aún no tienes cuenta?",
                        color = DarkText,
                        fontSize = 13.sp,
                        fontFamily = GlacialIndifference
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(
                        onClick = { },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Regístrate",
                            color = ForestDarkGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            fontFamily = GlacialIndifference
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        state = LoginUiState(),
        onCorreoChange = {},
        onAbrirRecorrido = {}
    )
}
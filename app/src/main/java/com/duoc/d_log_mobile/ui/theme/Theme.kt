package com.duoc.d_log_mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val coloresClaros = lightColorScheme(
    primary = Color(0xFF315C3C),
    secondary = Color(0xFF79583A),
    primaryContainer = Color(0xFFD5E8CB),
    background = Color(0xFFFAF7F0),
    surface = Color(0xFFFAF7F0),
    surfaceVariant = Color(0xFFE9E3D5)
)
private val coloresOscuros = darkColorScheme(
    primary = Color(0xFFA8CDA0),
    secondary = Color(0xFFDFC1A4),
    primaryContainer = Color(0xFF28452D),
    background = Color(0xFF181D17),
    surface = Color(0xFF181D17),
    surfaceVariant = Color(0xFF394035)
)

@Composable
fun DLogMobileTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) coloresOscuros else coloresClaros, typography = Typography, content = content)
}

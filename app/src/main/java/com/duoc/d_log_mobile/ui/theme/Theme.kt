package com.duoc.d_log_mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val coloresClaros = lightColorScheme(
    primary = VerdePrincipal,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = VerdeOscuro,
    secondary = MarronMadera,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0E0D0),
    onSecondaryContainer = Color(0xFF3E2723),
    tertiary = Color(0xFF4A6741),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC8E6C9),
    onTertiaryContainer = VerdeOscuro,
    background = FondoMadera,
    onBackground = Color(0xFF1C1B1F),
    surface = FondoMadera,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = SuperficieVarianteClaro,
    onSurfaceVariant = Color(0xFF49454F),
    error = ErrorRojo,
    onError = Color.White,
    outline = Color(0xFF7A8474)
)

private val coloresOscuros = darkColorScheme(
    primary = VerdeClaro,
    onPrimary = VerdeOscuro,
    primaryContainer = VerdeOscuro,
    onPrimaryContainer = VerdeContenedor,
    secondary = MarronClaro,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF5D4037),
    onSecondaryContainer = MarronClaro,
    tertiary = Color(0xFFA5D6A7),
    onTertiary = VerdeOscuro,
    tertiaryContainer = Color(0xFF2E4A32),
    onTertiaryContainer = Color(0xFFC8E6C9),
    background = FondoOscuro,
    onBackground = Color(0xFFE6E1E5),
    surface = FondoOscuro,
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = SuperficieVarianteOscuro,
    onSurfaceVariant = Color(0xFFCAC4D0),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    outline = Color(0xFF8A9388)
)

@Composable
fun DLogMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) coloresOscuros else coloresClaros,
        typography = Typography,
        content = content
    )
}
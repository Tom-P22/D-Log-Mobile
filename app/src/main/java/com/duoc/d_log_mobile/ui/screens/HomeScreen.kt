package com.duoc.d_log_mobile.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.duoc.d_log_mobile.ui.theme.DLogMobileTheme

// Pantalla base de Compose (guia 8). El catalogo se agregara mas adelante.
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(text = "Diseña Madera", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DLogMobileTheme {
        HomeScreen()
    }
}
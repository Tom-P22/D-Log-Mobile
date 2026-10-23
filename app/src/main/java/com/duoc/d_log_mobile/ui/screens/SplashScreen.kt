package com.duoc.d_log_mobile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.d_log_mobile.R
import com.duoc.d_log_mobile.ui.theme.VerdeClaro
import kotlinx.coroutines.delay

/**
 * Pantalla de inicio con logo personalizado D-Log y animación de entrada.
 * Verde oscuro reemplazado por un verde más claro (VerdeClaro / #4A7C59 en tema).
 */
@Composable
fun SplashScreen(
    onTerminado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.75f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(700))
        scale.animateTo(1f, animationSpec = tween(700))
        delay(1200)
        onTerminado()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo_bosque),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Scrim un poco más suave para que se note menos el verde oscuro de fondo
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF4A7C59).copy(alpha = 0.35f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha.value)
                .scale(scale.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(160.dp),
                shape = RoundedCornerShape(36.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                shadowElevation = 10.dp
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_dlog),
                    contentDescription = "Logo D-Log",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(36.dp))
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "D-Log",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Soluciones constructivas en madera",
                style = MaterialTheme.typography.bodyLarge,
                color = VerdeClaro,
                textAlign = TextAlign.Center
            )
        }
    }
}

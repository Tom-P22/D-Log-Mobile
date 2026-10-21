package com.duoc.d_log_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.duoc.d_log_mobile.ui.screens.DetailScreen
import com.duoc.d_log_mobile.ui.screens.HomeScreen
import com.duoc.d_log_mobile.ui.theme.DLogMobileTheme
import com.duoc.d_log_mobile.viewmodel.SolucionViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SolucionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DLogMobileTheme {
                val state by viewModel.uiState.collectAsState()
                var solucionSeleccionadaId by remember { mutableStateOf<String?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (solucionSeleccionadaId == null) {
                        HomeScreen(
                            state = state,
                            onBusquedaChange = { texto -> viewModel.actualizarBusqueda(texto) },
                            onCategoriaSelect = { cat -> viewModel.seleccionarCategoria(cat) },
                            onDetalle = { id -> solucionSeleccionadaId = id },
                            onFavoritos = { },
                            onNuevaConsulta = { },
                            onSalir = { },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        val solucion = viewModel.obtenerSolucion(solucionSeleccionadaId!!)
                        DetailScreen(
                            solucion = solucion,
                            esFavorito = state.favoritos.contains(solucionSeleccionadaId),
                            onVolver = { solucionSeleccionadaId = null },
                            onToggleFavorito = { viewModel.alternarFavorito(solucionSeleccionadaId!!) },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
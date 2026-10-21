package com.duoc.d_log_mobile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        state = state,
                        onBusquedaChange = { texto -> viewModel.actualizarBusqueda(texto) },
                        onCategoriaSelect = { cat -> viewModel.seleccionarCategoria(cat) },
                        onDetalle = { id ->
                            Toast.makeText(this, "Seleccionado ID: $id", Toast.LENGTH_SHORT).show()
                        },
                        onFavoritos = { },
                        onNuevaConsulta = { },
                        onSalir = { },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
package com.duoc.d_log_mobile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.d_log_mobile.ui.screens.ConsultaScreen
import com.duoc.d_log_mobile.ui.screens.DetalleScreen
import com.duoc.d_log_mobile.ui.screens.FavoritosScreen
import com.duoc.d_log_mobile.ui.screens.HomeScreen
import com.duoc.d_log_mobile.ui.screens.LoginScreen
import com.duoc.d_log_mobile.ui.screens.ResumenScreen
import com.duoc.d_log_mobile.viewmodel.ConsultaViewModel
import com.duoc.d_log_mobile.viewmodel.LoginViewModel
import com.duoc.d_log_mobile.viewmodel.SolucionViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Se crean una sola vez aquí para compartirlos entre las pantallas.
    val loginViewModel: LoginViewModel = viewModel()
    val solucionViewModel: SolucionViewModel = viewModel()
    val consultaViewModel: ConsultaViewModel = viewModel()

    // Cuando cambia un StateFlow, Compose actualiza lo que muestra la pantalla.
    val loginState by loginViewModel.uiState.collectAsState()
    val solucionState by solucionViewModel.uiState.collectAsState()
    val consultaState by consultaViewModel.uiState.collectAsState()

    NavHost(navController, startDestination = Routes.LOGIN, modifier = modifier) {
        composable(Routes.LOGIN) {
            LoginScreen(
                state = loginState,
                onCorreoChange = { loginViewModel.actualizarCorreo(it) },
                onAbrirRecorrido = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                state = solucionState,
                onDetalle = { navController.navigate(Routes.detalle(it)) },
                onFavoritos = { navController.navigate(Routes.FAVORITOS) },
                onConsulta = {
                    consultaViewModel.limpiarConsulta()
                    navController.navigate(Routes.CONSULTA)
                },
                onSalir = {
                    loginViewModel.limpiarCorreo()
                    solucionViewModel.limpiarFavoritos()
                    consultaViewModel.limpiarConsulta()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Routes.DETALLE,
            arguments = listOf(navArgument(Routes.SOLUCION_ID) { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString(Routes.SOLUCION_ID).orEmpty()
            DetalleScreen(
                solucion = solucionViewModel.obtenerSolucion(id),
                esFavorita = id in solucionState.favoritos,
                onFavorito = { solucionViewModel.alternarFavorito(id) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Routes.FAVORITOS) {
            FavoritosScreen(
                soluciones = solucionState.solucionesFavoritas,
                onDetalle = { navController.navigate(Routes.detalle(it)) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Routes.CONSULTA) {
            ConsultaScreen(
                state = consultaState,
                onNombreChange = { consultaViewModel.actualizarNombre(it) },
                onCorreoChange = { consultaViewModel.actualizarCorreo(it) },
                onCategoriaChange = { consultaViewModel.actualizarCategoria(it) },
                onDescripcionChange = { consultaViewModel.actualizarDescripcion(it) },
                onResumen = { navController.navigate(Routes.RESUMEN) },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Routes.RESUMEN) {
            ResumenScreen(
                state = consultaState,
                onEditar = { navController.popBackStack() },
                onFinalizar = {
                    consultaViewModel.limpiarConsulta()
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }
    }
}

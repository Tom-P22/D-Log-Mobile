package com.duoc.d_log_mobile.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import com.duoc.d_log_mobile.ui.screens.*
import com.duoc.d_log_mobile.viewmodel.ConsultaViewModel
import com.duoc.d_log_mobile.viewmodel.LoginViewModel
import com.duoc.d_log_mobile.viewmodel.SolucionViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val contexto = LocalContext.current.applicationContext
    val preferencias = remember { PreferenciasRepository(contexto) }
    val loginViewModel: LoginViewModel = viewModel { LoginViewModel(preferencias) }
    val solucionViewModel: SolucionViewModel = viewModel { SolucionViewModel(preferencias = preferencias) }
    val consultaViewModel: ConsultaViewModel = viewModel()
    val loginState by loginViewModel.uiState.collectAsState()
    val solucionState by solucionViewModel.uiState.collectAsState()
    val consultaState by consultaViewModel.uiState.collectAsState()
    val sesion = loginState.sesionIniciada

    // Se espera a leer DataStore antes de elegir la pantalla inicial.
    if (sesion == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    LaunchedEffect(sesion) {
        if (!sesion) {
            consultaViewModel.limpiarConsulta()
            solucionViewModel.reiniciarFiltros()
        }
    }

    // Al entrar o salir se crea una pila nueva: Atrás no recupera una sesión cerrada.
    key(sesion) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = if (sesion) Routes.HOME else Routes.LOGIN, modifier = modifier) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    state = loginState,
                    onCorreoChange = { loginViewModel.actualizarCorreo(it) },
                    onClaveChange = { loginViewModel.actualizarClave(it) },
                    onIniciarSesion = { loginViewModel.iniciarSesion() }
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    state = solucionState,
                    onBusquedaChange = { solucionViewModel.actualizarBusqueda(it) },
                    onCategoriaSelect = { solucionViewModel.seleccionarCategoria(it) },
                    onDetalle = { navController.navigate(Routes.detalle(it)) { launchSingleTop = true } },
                    onFavoritos = { navController.navigate(Routes.FAVORITOS) { launchSingleTop = true } },
                    onConsulta = {
                        consultaViewModel.limpiarConsulta()
                        navController.navigate(Routes.CONSULTA) { launchSingleTop = true }
                    },
                    onSalir = { loginViewModel.cerrarSesion() },
                    saliendo = loginState.cargando,
                    mensajeSesion = loginState.mensaje
                )
            }
            composable(
                Routes.DETALLE,
                arguments = listOf(navArgument(Routes.SOLUCION_ID) { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString(Routes.SOLUCION_ID).orEmpty()
                DetalleScreen(
                    solucion = solucionViewModel.obtenerSolucion(id),
                    esFavorita = id in solucionState.favoritos,
                    onFavorito = { solucionViewModel.alternarFavorito(id) },
                    onVolver = { navController.popBackStack() },
                    guardando = solucionState.guardandoFavorito || solucionState.cargando,
                    mensaje = solucionState.mensajeParaSolucion(id)
                )
            }
            composable(Routes.FAVORITOS) {
                FavoritosScreen(
                    soluciones = solucionState.solucionesFavoritas,
                    onDetalle = { navController.navigate(Routes.detalle(it)) { launchSingleTop = true } },
                    onVolver = { navController.popBackStack() },
                    cargando = solucionState.cargando
                )
            }
            composable(Routes.CONSULTA) {
                ConsultaScreen(
                    state = consultaState,
                    onNombreChange = { consultaViewModel.actualizarNombre(it) },
                    onCorreoChange = { consultaViewModel.actualizarCorreo(it) },
                    onCategoriaChange = { consultaViewModel.actualizarCategoria(it) },
                    onDescripcionChange = { consultaViewModel.actualizarDescripcion(it) },
                    onResumen = {
                        if (consultaViewModel.validarFormulario()) {
                            navController.navigate(Routes.RESUMEN) { launchSingleTop = true }
                        }
                    },
                    onVolver = { navController.popBackStack() },
                    onImagenChange = { consultaViewModel.actualizarImagen(it) },
                    onPrepararFoto = { consultaViewModel.prepararFoto(it) },
                    onRecibirFoto = { consultaViewModel.recibirFoto(it) },
                    onErrorImagen = { consultaViewModel.mostrarErrorImagen() }
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
}

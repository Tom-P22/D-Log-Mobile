package com.duoc.d_log_mobile.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.LocalActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duoc.d_log_mobile.repository.PreferenciasRepository
import com.duoc.d_log_mobile.ui.screens.*
import com.duoc.d_log_mobile.viewmodel.ConsultaViewModel
import com.duoc.d_log_mobile.viewmodel.LoginViewModel
import com.duoc.d_log_mobile.viewmodel.SolucionViewModel

private fun NavController.volverSeguro(destinoSeguro: String = Routes.HOME) {
    if (previousBackStackEntry != null) {
        popBackStack()
    } else {
        navigate(destinoSeguro) {
            launchSingleTop = true
            popUpTo(graph.startDestinationId) {
                inclusive = false
            }
        }
    }
}

@Composable
fun AppNavigation(
    mostrarSplashInicial: Boolean = true,
    modifier: Modifier = Modifier
) {
    val activity = LocalActivity.current
    val appContext = LocalContext.current.applicationContext
    val preferencias = remember { PreferenciasRepository(appContext) }
    val loginViewModel: LoginViewModel = viewModel { LoginViewModel(preferencias) }
    val solucionViewModel: SolucionViewModel = viewModel { SolucionViewModel(preferencias = preferencias) }
    val consultaViewModel: ConsultaViewModel = viewModel()
    val loginState by loginViewModel.uiState.collectAsState()
    val solucionState by solucionViewModel.uiState.collectAsState()
    val consultaState by consultaViewModel.uiState.collectAsState()
    val sesion = loginState.sesionIniciada

    // Solo true en arranque en frío (savedInstanceState == null).
    // Así al volver de la cámara no se muestra de nuevo el splash ni parece que la app reinicia.
    var mostrarSplash by remember { mutableStateOf(mostrarSplashInicial) }
    if (mostrarSplash) {
        SplashScreen(onTerminado = { mostrarSplash = false }, modifier = modifier)
        return
    }

    if (sesion == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    LaunchedEffect(sesion) {
        if (!sesion) {
            consultaViewModel.limpiarConsulta()
            solucionViewModel.reiniciarFiltros()
        }
    }

    key(sesion) {
        val navController = rememberNavController()
        val destinoBase = if (sesion) Routes.HOME else Routes.LOGIN
        val backStackEntry by navController.currentBackStackEntryAsState()
        val rutaActual = backStackEntry?.destination?.route

        val enRaiz = rutaActual == null ||
                rutaActual == Routes.HOME ||
                rutaActual == Routes.LOGIN
        BackHandler(enabled = enRaiz) {
            activity?.finish()
        }

        NavHost(
            navController = navController,
            startDestination = destinoBase,
            modifier = modifier
        ) {
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
                    onDetalle = {
                        navController.navigate(Routes.detalle(it)) { launchSingleTop = true }
                    },
                    onFavoritos = {
                        navController.navigate(Routes.FAVORITOS) { launchSingleTop = true }
                    },
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
                route = Routes.DETALLE,
                arguments = listOf(navArgument(Routes.SOLUCION_ID) { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString(Routes.SOLUCION_ID).orEmpty()
                DetalleScreen(
                    solucion = solucionViewModel.obtenerSolucion(id),
                    esFavorita = id in solucionState.favoritos,
                    onFavorito = { solucionViewModel.alternarFavorito(id) },
                    onVolver = { navController.volverSeguro(Routes.HOME) },
                    guardando = solucionState.guardandoFavorito || solucionState.cargando,
                    mensaje = solucionState.mensaje
                )
            }
            composable(Routes.FAVORITOS) {
                FavoritosScreen(
                    soluciones = solucionState.solucionesFavoritas,
                    onDetalle = {
                        navController.navigate(Routes.detalle(it)) { launchSingleTop = true }
                    },
                    onVolver = { navController.volverSeguro(Routes.HOME) },
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
                    onVolver = { navController.volverSeguro(Routes.HOME) },
                    onImagenChange = { consultaViewModel.actualizarImagen(it) },
                    onPrepararFoto = { consultaViewModel.prepararFoto(it) },
                    onRecibirFoto = { consultaViewModel.recibirFoto(it) },
                    onErrorImagen = { consultaViewModel.mostrarErrorImagen() }
                )
            }
            composable(Routes.RESUMEN) {
                ResumenScreen(
                    state = consultaState,
                    onEditar = {
                        if (!navController.popBackStack(Routes.CONSULTA, inclusive = false)) {
                            navController.navigate(Routes.CONSULTA) { launchSingleTop = true }
                        }
                    },
                    onFinalizar = {
                        consultaViewModel.limpiarConsulta()
                        navController.navigate(Routes.HOME) {
                            launchSingleTop = true
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
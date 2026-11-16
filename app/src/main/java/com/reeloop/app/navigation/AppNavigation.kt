package com.reeloop.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reeloop.app.presentation.common.BarraInferior
import com.reeloop.app.presentation.event.ObjetoEvent
import com.reeloop.app.presentation.screens.DetalleScreen
import com.reeloop.app.presentation.screens.ExplorarScreen
import com.reeloop.app.presentation.screens.ImpactoScreen
import com.reeloop.app.presentation.screens.InicioScreen
import com.reeloop.app.presentation.screens.LoginScreen
import com.reeloop.app.presentation.screens.MisObjetosScreen
import com.reeloop.app.presentation.screens.PasaporteScreen
import com.reeloop.app.presentation.screens.PerfilScreen
import com.reeloop.app.presentation.screens.PublicarScreen
import com.reeloop.app.presentation.screens.CodigoQrScreen
import com.reeloop.app.presentation.screens.EscanearQrScreen
import com.reeloop.app.presentation.screens.RegistroScreen
import com.reeloop.app.presentation.screens.SolicitudesScreen
import com.reeloop.app.presentation.viewmodel.ObjetoViewModel
import com.reeloop.app.ui.theme.Fondo

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val vm: ObjetoViewModel = viewModel()
    val state by vm.state.collectAsState()
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route

    val irAPestana: (String) -> Unit = { destino ->
        nav.navigate(destino) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            if (ruta != null && ruta !in Rutas.sinBarra) {
                BarraInferior(seleccionada = Rutas.pestanaDe(ruta), onClick = irAPestana)
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Rutas.LOGIN,
            modifier = Modifier.padding(padding)
        ) {
            composable(Rutas.LOGIN) {
                LoginScreen(
                    onEntrar = {
                        nav.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onCrearCuenta = { nav.navigate(Rutas.REGISTRO) }
                )
            }
            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onRegistrarme = {
                        nav.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onYatengoCuenta = { nav.popBackStack() }
                )
            }
            composable(Rutas.INICIO) {
                InicioScreen(
                    state = state,
                    onPublicar = { nav.navigate(Rutas.PUBLICAR) },
                    onExplorar = { nav.navigate(Rutas.EXPLORAR) },
                    onMisObjetos = { nav.navigate(Rutas.MIS_OBJETOS) },
                    onSolicitudes = { nav.navigate(Rutas.SOLICITUDES) },
                    onEscanear = { nav.navigate(Rutas.ESCANEAR_QR) }
                )
            }
            composable(Rutas.EXPLORAR) {
                ExplorarScreen(
                    state = state,
                    onEvent = vm::onEvent,
                    onVerDetalle = { id -> nav.navigate(Rutas.detalle(id)) }
                )
            }
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                DetalleScreen(
                    objeto = state.listaObjeto.find { it.id == id },
                    onVolver = { nav.popBackStack() },
                    onSolicitar = { vm.onEvent(ObjetoEvent.OnSolicitar(id)) },
                    onPasaporte = { nav.navigate(Rutas.pasaporte(id)) }
                )
            }
            composable(Rutas.PUBLICAR) {
                PublicarScreen(
                    state = state,
                    onEvent = vm::onEvent,
                    onPublicado = { 
                        nav.navigate(Rutas.MIS_OBJETOS) {
                            popUpTo(Rutas.INICIO)
                        } 
                    }
                )
            }
            composable(Rutas.MIS_OBJETOS) {
                MisObjetosScreen(
                    state = state,
                    onEvent = vm::onEvent,
                    onPublicar = { nav.navigate(Rutas.PUBLICAR) },
                    onVerDetalle = { id -> nav.navigate(Rutas.detalle(id)) }
                )
            }
            composable(
                route = Rutas.CODIGO_QR,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                CodigoQrScreen(
                    objeto = state.listaObjeto.find { it.id == id },
                    onVolver = { nav.popBackStack() }
                )
            }
            composable(Rutas.ESCANEAR_QR) {
                EscanearQrScreen(onVolver = { nav.popBackStack() })
            }
            composable(
                route = Rutas.PASAPORTE,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                PasaporteScreen(
                    objeto = state.listaObjeto.find { it.id == id },
                    onVolver = { nav.popBackStack() }
                )
            }
            composable(Rutas.IMPACTO) {
                ImpactoScreen(state = state)
            }
            composable(Rutas.PERFIL) {
                PerfilScreen(
                    state = state,
                    onMisObjetos = { nav.navigate(Rutas.MIS_OBJETOS) },
                    onSolicitudes = { nav.navigate(Rutas.SOLICITUDES) },
                    onCerrarSesion = {
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.INICIO) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Rutas.SOLICITUDES) {
                SolicitudesScreen(
                    state = state,
                    onEvent = vm::onEvent,
                    onVolver = { nav.popBackStack() },
                    onVerQr = { id -> nav.navigate(Rutas.codigoQr(id)) }
                )
            }
        }
    }
}

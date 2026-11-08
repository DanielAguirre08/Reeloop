package com.reeloop.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.reeloop.app.presentation.common.BarraInferior
import com.reeloop.app.presentation.screens.PantallaVacia
import com.reeloop.app.presentation.screens.login.LoginScreen
import com.reeloop.app.presentation.screens.misobjetos.MisObjetosScreen
import com.reeloop.app.presentation.screens.publicar.PublicarScreen
import com.reeloop.app.presentation.screens.registro.RegistroScreen
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.presentation.viewmodel.ObjetoViewModel
import kotlin.io.encoding.Base64

@Composable
fun AppNavigation(){

    val nav= rememberNavController()
    val vm: ObjetoViewModel = viewModel()
    val state by vm.state.collectAsState()
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route

    val irA:(String) -> Unit = {destino -> nav.navigate(destino)}
    val irAPestana: (String) -> Unit = {destino ->
        nav.navigate(destino) {
            popUpTo(Rutas.INICIO){saveState =true}
            launchSingleTop= true
            restoreState = true
        }

    }

    Scaffold(
        bottomBar = {
            if (ruta != null && ruta !in Rutas.sinBarra){
                BarraInferior(seleccionada = Rutas.pestanaDe(ruta), irAPestana,)

            }
        }
    ) { padding ->
        NavHost (
            navController =nav,
            startDestination =Rutas.LOGIN,
            modifier = Modifier.padding(padding)
        ){
            composable(Rutas.LOGIN) {
                LoginScreen(
                    onEntrar = {
                        nav.navigate(Rutas.INICIO) { popUpTo(Rutas.LOGIN) { inclusive = true } }
                    },
                    onCrearCuenta = { nav.navigate(Rutas.REGISTRO) }
                )            }
            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onRegistrarme = {
                        nav.navigate(Rutas.INICIO){popUpTo(Rutas.LOGIN){inclusive =true} }},
                    onYatengoCuenta = {nav.popBackStack()}
                )

            }
            composable(Rutas.INICIO) {
                PantallaVacia("Inicio", listOf("Mis objetos" to Rutas.MIS_OBJETOS, "Solicitudes" to Rutas.SOLICITUDES), irA)
            }
            composable(Rutas.EXPLORAR) {
                PantallaVacia("Explorar", listOf("Ver detalle" to Rutas.DETALLE), irA)
            }
            composable(Rutas.DETALLE) {
                PantallaVacia("Detalle", listOf("Ver pasaporte" to Rutas.PASAPORTE), irA)
            }
            composable(Rutas.PUBLICAR) {
                PublicarScreen(
                    state = state,
                    onEvent = vm::onEvent,
                    onPublicado = { nav.navigate(Rutas.MIS_OBJETOS) }
                )
            }
            composable(Rutas.MIS_OBJETOS) {
                MisObjetosScreen(
                    state = state,
                    onEvent = vm::onEvent,
                    onPublicar = { nav.navigate(Rutas.PUBLICAR) }
                )            }
            composable(Rutas.CODIGO_QR) { PantallaVacia("Código QR") }
            composable(Rutas.ESCANEAR_QR) { PantallaVacia("Escanear QR") }
            composable(Rutas.PASAPORTE) { PantallaVacia("Pasaporte") }
            composable(Rutas.IMPACTO) { PantallaVacia("EcoImpacto") }
            composable(Rutas.PERFIL) {
                PantallaVacia("Perfil", listOf("Solicitudes" to Rutas.SOLICITUDES), irA)
            }
            composable(Rutas.SOLICITUDES) {
                PantallaVacia("Solicitudes", listOf("Ver código QR" to Rutas.CODIGO_QR), irA)
            }


        }


    }

}



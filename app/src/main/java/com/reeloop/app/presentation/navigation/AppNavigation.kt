package com.reeloop.app.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.presentation.screens.codigoqr.CodigoQrScreen
import com.reeloop.app.presentation.screens.detalle.DetalleScreen
import com.reeloop.app.presentation.screens.escanearqr.EscanearQrScreen
import com.reeloop.app.presentation.screens.explorar.ExplorarScreen
import com.reeloop.app.presentation.screens.impacto.ImpactoScreen
import com.reeloop.app.presentation.screens.inicio.InicioScreen
import com.reeloop.app.presentation.screens.login.LoginScreen
import com.reeloop.app.presentation.screens.misobjetos.MisObjetosScreen
import com.reeloop.app.presentation.screens.pasaporte.PasaporteScreen
import com.reeloop.app.presentation.screens.perfil.PerfilScreen
import com.reeloop.app.presentation.screens.publicar.PublicarScreen
import com.reeloop.app.presentation.screens.registro.RegistroScreen
import com.reeloop.app.presentation.screens.solicitudes.SolicitudesScreen

@Composable

@Composable
fun AppNavigation(navController: NavHostController, sesion: SesionRepository) {

    // Ir a una pestaña de la barra inferior sin apilar pantallas repetidas
    val irAPestana: (String) -> Unit = { destino ->
        navController.navigate(destino) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Entrar a la app: va a Inicio y borra Login/Registro de la pila
    val irAInicio: () -> Unit = {
        navController.navigate(Rutas.INICIO) {
            popUpTo(Rutas.LOGIN) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (sesion.usuario.value != null) Rutas.INICIO else Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                onSesionIniciada = irAInicio,
                onCrearCuenta = { navController.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistrado = irAInicio,
                onYaTengoCuenta = { navController.popBackStack() }
            )
        }
        composable(Rutas.INICIO) {
            InicioScreen(
                onPublicar = { irAPestana(Rutas.PUBLICAR) },
                onExplorar = { irAPestana(Rutas.EXPLORAR) },
                onMisObjetos = { navController.navigate(Rutas.MIS_OBJETOS) },
                onSolicitudes = { navController.navigate(Rutas.SOLICITUDES) },
                onEscanear = { navController.navigate(Rutas.escanearQr()) }
            )
        }
        composable(Rutas.EXPLORAR) {
            ExplorarScreen(onVerDetalle = { id -> navController.navigate(Rutas.detalle(id)) })
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            DetalleScreen(
                onPasaporte = { id -> navController.navigate(Rutas.pasaporte(id)) },
                onEliminado = { navController.popBackStack() }
            )
        }
        composable(Rutas.PUBLICAR) {
            PublicarScreen(
                onPublicado = {
                    navController.navigate(Rutas.MIS_OBJETOS) { popUpTo(Rutas.INICIO) }
                }
            )
        }
        composable(Rutas.MIS_OBJETOS) {
            MisObjetosScreen(
                onPublicar = { irAPestana(Rutas.PUBLICAR) },
                onVerDetalle = { id -> navController.navigate(Rutas.detalle(id)) }
            )
        }
        composable(Rutas.IMPACTO) { ImpactoScreen() }
        composable(Rutas.PERFIL) {
            PerfilScreen(
                onMisObjetos = { navController.navigate(Rutas.MIS_OBJETOS) },
                onSolicitudes = { navController.navigate(Rutas.SOLICITUDES) },
                onSesionCerrada = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.SOLICITUDES) {
            SolicitudesScreen(
                onVerQr = { id -> navController.navigate(Rutas.codigoQr(id)) },
                onConfirmarEntrega = { id -> navController.navigate(Rutas.escanearQr(id)) }
            )
        }
        composable(
            route = Rutas.CODIGO_QR,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { CodigoQrScreen() }
        composable(
            route = Rutas.ESCANEAR_QR,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            EscanearQrScreen(
                onEntregado = { id ->
                    navController.navigate(Rutas.pasaporte(id)) {
                        popUpTo(Rutas.ESCANEAR_QR) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Rutas.PASAPORTE,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { PasaporteScreen() }
    }
}
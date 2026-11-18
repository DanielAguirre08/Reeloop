package com.reeloop.app.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.presentation.event.UiEvent
import com.reeloop.app.presentation.event.UIeventBus
import com.reeloop.app.ui.theme.Fondo
import kotlinx.coroutines.flow.collectLatest
@Composable
fun AppScaffold(
    navController: NavHostController,
    eventBus: UIeventBus,
    sesion: SesionRepository,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarTipo by remember { mutableStateOf(SnackbarTipo.DEFAULT) }

    LaunchedEffect(Unit) {
        eventBus.eventos.collectLatest { evento ->
            when (evento) {
                is UiEvent.MostrarSnackbar -> {
                    snackbarTipo = evento.tipo
                    snackbarHostState.showSnackbar(evento.mensaje)
                }
            }
        }
    }

    Scaffold(
        containerColor = Fondo,
        snackbarHost = { SnackbarCustom(hostState = snackbarHostState, tipo = snackbarTipo) },
        topBar = { ApptopBar(navController, sesion) },
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        content(paddingValues)
    }
}
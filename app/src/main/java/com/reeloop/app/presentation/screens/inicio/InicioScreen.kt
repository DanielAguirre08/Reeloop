package com.reeloop.app.presentation.screens.inicio


import androidx.compose.runtime.Composable
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun InicioScreen(
    onPublicar: () -> Unit,
    onExplorar: () -> Unit,
    onMisObjetos: () -> Unit,
    onSolicitudes: () -> Unit,
    onEscanear: () -> Unit
) {
    PantallaPendiente("Inicio", "Módulo B")
}
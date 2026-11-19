package com.reeloop.app.presentation.screens.detalle

import androidx.compose.runtime.Composable
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun DetalleScreen(onPasaporte: (String) -> Unit, onEliminado: () -> Unit) {
    PantallaPendiente("Detalle", "Módulo C")
}
package com.reeloop.app.presentation.screens.solicitudes
import androidx.compose.runtime.Composable
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun SolicitudesScreen(onVerQr: (String) -> Unit, onConfirmarEntrega: (String) -> Unit) {
    PantallaPendiente("Solicitudes", "Módulo D")
}
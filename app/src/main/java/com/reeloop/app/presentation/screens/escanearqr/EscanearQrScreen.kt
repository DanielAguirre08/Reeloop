package com.reeloop.app.presentation.screens.escanearqr

import androidx.compose.runtime.Composable
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun EscanearQrScreen(onEntregado: (String) -> Unit) {
    PantallaPendiente("Escanear QR", "Módulo D")
}
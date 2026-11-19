package com.reeloop.app.presentation.screens.misobjetos

import androidx.compose.runtime.Composable
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun MisObjetosScreen(onPublicar: () -> Unit, onVerDetalle: (String) -> Unit) {
    PantallaPendiente("Mis objetos", "Módulo C")
}
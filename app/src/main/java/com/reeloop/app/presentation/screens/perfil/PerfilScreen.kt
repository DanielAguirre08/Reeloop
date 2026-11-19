package com.reeloop.app.presentation.screens.perfil

import androidx.compose.runtime.Composable
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun PerfilScreen(
    onMisObjetos: () -> Unit,
    onSolicitudes: () -> Unit,
    onSesionCerrada: () -> Unit
) {
    PantallaPendiente("Perfil", "Módulo B")
}

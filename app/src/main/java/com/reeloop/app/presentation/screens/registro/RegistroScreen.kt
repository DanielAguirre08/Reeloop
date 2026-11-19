package com.reeloop.app.presentation.screens.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.PantallaPendiente

@Composable
fun RegistroScreen(onRegistrado: () -> Unit, onYaTengoCuenta: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PantallaPendiente("Registro", "Módulo B", modifier = Modifier)
        BotonPrincipal("Registrarme (prueba)", onRegistrado)
        BotonSecundario("Ya tengo cuenta", onYaTengoCuenta)
    }
}

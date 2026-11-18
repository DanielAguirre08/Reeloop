package com.reeloop.app.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reeloop.app.ui.theme.Rojo
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun SnackbarCustom(hostState: SnackbarHostState, tipo: SnackbarTipo){
    SnackbarHost(hostState = hostState){data ->
        val colorFondo = when (tipo) {
            SnackbarTipo.SUCCESS -> VerdeOscuro
            SnackbarTipo.ERROR -> Rojo
            SnackbarTipo.WARNING -> Color(0xFFF57C00)
            SnackbarTipo.INFO -> Color(0xFF0288D1)
            SnackbarTipo.DEFAULT -> Color(0xFF000000)
    }
        Surface( modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = colorFondo,
            contentColor = Color.White,
            shadowElevation = 6.dp) {
        }
        Text(
            text = data.visuals.message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        }

}
package com.reeloop.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.GrisClaro
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun Etiqueta(texto: String, destacada: Boolean = false) {
    Text(
        text = texto,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = if (destacada) VerdeOscuro else TextoGris,
        modifier = Modifier
            .background(if (destacada) VerdeClaro else GrisClaro, CircleShape)
            .border(1.dp, if (destacada) VerdePrincipal else Borde, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

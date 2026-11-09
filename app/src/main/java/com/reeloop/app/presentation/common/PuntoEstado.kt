package com.reeloop.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reeloop.app.ui.theme.Rojo
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun PuntoEstado(texto: String, activo: Boolean = true) {
    Row(
        modifier = Modifier
            .background(VerdeClaro, CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(10.dp).background(if (activo) VerdePrincipal else Rojo, CircleShape))
        Text(texto, color = VerdeOscuro, fontWeight = FontWeight.Medium)
    }
}
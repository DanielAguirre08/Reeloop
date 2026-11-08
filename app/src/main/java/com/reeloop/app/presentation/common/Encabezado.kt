package com.reeloop.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun Encabezado(titulo: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LogoReeloop(tamaño = 40.dp)
        Text(
            text = titulo,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro,
            modifier = Modifier.weight(1f)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(40.dp).background(VerdeClaro, CircleShape)
        ) {
            Text("A", color = VerdeOscuro, fontWeight = FontWeight.Bold)
        }
    }
}

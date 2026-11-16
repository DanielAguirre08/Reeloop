package com.reeloop.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun CodigoQrScreen(objeto: Objeto?, onVolver: () -> Unit) {
    var version by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        EncabezadoAtras("Código QR", onVolver)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("QR de entrega", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Text(objeto?.nombre ?: "", color = TextoGris, textAlign = TextAlign.Center)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(260.dp).background(Color.White, RoundedCornerShape(28.dp)).padding(20.dp)
        ) {
            Icon(
                Icons.Default.QrCode2,
                contentDescription = "Código QR",
                tint = VerdeOscuro,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(Modifier.weight(1f))
        BotonPrincipal("Regenerar vista", onClick = { version += 1 }, Modifier.fillMaxWidth())
    }
}

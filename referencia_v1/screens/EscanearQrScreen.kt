package com.reeloop.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun EscanearQrScreen(onVolver: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        EncabezadoAtras("Escanear QR", onVolver)
        Text("Escanea el código", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().height(340.dp).background(VerdeOscuro, RoundedCornerShape(28.dp))
        ) {
            Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = "Marco de escaneo",
                tint = VerdeClaro,
                modifier = Modifier.size(230.dp)
            )
        }

        Box(Modifier.weight(1f))
        BotonSecundario("Cancelar escaneo", onVolver, Modifier.fillMaxWidth())
    }
}

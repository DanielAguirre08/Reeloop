package com.reeloop.app.presentation.screens.qr
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.presentation.common.PuntoEstado
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdePrincipal
import com.reeloop.app.util.QrGenerador
import kotlinx.coroutines.delay

@Composable
fun CodigoQrScreen(
    objeto: Objeto?,
    onVolver: () -> Unit,
    onEscanear: () -> Unit
) {
    val id = objeto?.id ?: 0
    var intento by remember { mutableIntStateOf(1) }
    var segundos by remember { mutableIntStateOf(900) }   // 15 minutos

    // Cada vez que cambia "intento" (Regenerar), reinicia la cuenta regresiva
    LaunchedEffect(intento) {
        segundos = 900
        while (segundos > 0) {
            delay(1000)
            segundos--
        }
    }

    val contenido = "reloop:objeto:$id:$intento"
    val qr = remember(contenido) { QrGenerador.generar(contenido) }
    val vencido = segundos == 0
    val tiempo = "%02d:%02d".format(segundos / 60, segundos % 60)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        EncabezadoAtras("Código QR", onVolver)

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
        ) {
            Text("QR de entrega", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            if (objeto != null) Text(objeto.nombre, color = TextoGris)
            Text(
                text = if (vencido) "Código vencido · genera uno nuevo" else "Código de un solo uso · vence en $tiempo",
                color = TextoGris
            )
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(28.dp))
                    .border(1.dp, Borde, RoundedCornerShape(28.dp))
                    .padding(24.dp)
            ) {
                Image(
                    bitmap = qr,
                    contentDescription = "Código QR de entrega",
                    modifier = Modifier.size(240.dp).alpha(if (vencido) 0.25f else 1f)
                )
            }
            PuntoEstado(
                texto = if (vencido) "Vencido" else "Listo para ser escaneado",
                activo = !vencido
            )
        }

        BotonPrincipal("Regenerar QR", onClick = { intento++ })
        TextButton(onClick = onEscanear, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Simular que el receptor escanea", color = VerdePrincipal, fontWeight = FontWeight.SemiBold)
        }
    }
}
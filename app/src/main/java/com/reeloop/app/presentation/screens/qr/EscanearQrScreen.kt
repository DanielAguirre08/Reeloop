package com.reeloop.app.presentation.screens.qr

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.presentation.common.PuntoEstado
import com.reeloop.app.ui.theme.FondoCamara
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeMenta
import com.reeloop.app.util.QrGenerador

@Composable
fun EscanearQrScreen(
    onVolver: () -> Unit,
    onConfirmar: () -> Unit
) {
    // Animación infinita: la línea de escaneo sube y baja
    val transicion = rememberInfiniteTransition(label = "escaneo")
    val avance by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Reverse),
        label = "avance"
    )
    val qr = remember { QrGenerador.generar("reloop:demo") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EncabezadoAtras("Escanear QR", onVolver)
        Text(
            "Escanea el código de ReLoop",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro,
            textAlign = TextAlign.Center
        )
        Text(
            "Alinea el QR dentro del marco para confirmar.",
            color = TextoGris,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .background(FondoCamara, RoundedCornerShape(32.dp))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(180.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                Image(bitmap = qr, contentDescription = null, modifier = Modifier.fillMaxSize())
            }
            EsquinasMarco(Modifier.matchParentSize().padding(24.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 24.dp)
                    .offset(y = 30.dp + (avance * 280f).dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(VerdeMenta)
            )
        }

        PuntoEstado("Cámara activa · buscando código")

        Spacer(Modifier.weight(1f))
        BotonPrincipal("Simular escaneo", onClick = onConfirmar)
        BotonSecundario("Cancelar escaneo", onClick = onVolver)
    }
}

// Las 4 esquinas verdes del marco
@Composable
private fun EsquinasMarco(modifier: Modifier) {
    Canvas(modifier) {
        val largo = 48.dp.toPx()
        val grosor = 5.dp.toPx()
        val w = size.width
        val h = size.height

        fun linea(x1: Float, y1: Float, x2: Float, y2: Float) =
            drawLine(VerdeMenta, Offset(x1, y1), Offset(x2, y2), grosor, StrokeCap.Round)

        linea(0f, 0f, largo, 0f); linea(0f, 0f, 0f, largo)
        linea(w, 0f, w - largo, 0f); linea(w, 0f, w, largo)
        linea(0f, h, largo, h); linea(0f, h, 0f, h - largo)
        linea(w, h, w - largo, h); linea(w, h, w, h - largo)
    }
}
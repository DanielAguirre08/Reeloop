package com.reeloop.app.presentation.screens.solicitudes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.presentation.common.Etiqueta
import com.reeloop.app.presentation.common.colorCategoria
import com.reeloop.app.presentation.event.ObjetoEvent
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.util.Fechas

@Composable
fun SolicitudesScreen(
    state: ObjetoState,
    onEvent: (ObjetoEvent) -> Unit,
    onVolver: () ->Unit,
    onVerQr:(Int) -> Unit
){
    val solicitudes =state.listaObjeto
        .filter { it.estado == Opciones.SOLICITADO || it.estado == Opciones.ACEPTADO }
        .sortedByDescending { it.fechaSolicitado }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { EncabezadoAtras("Solicitudes", onVolver) }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Solicitudes recibidas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                    Text("Revisa quién necesita tus objetos publicados", fontSize = 14.sp, color = TextoGris)
                }
                Etiqueta("${solicitudes.size} activas", destacada = true)
            }
        }

        if (solicitudes.isEmpty()) {
            item {
                Text(
                    text = "No tienes solicitudes por ahora.",
                    color = TextoGris,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                )
            }
        } else {
            items(solicitudes, key = { it.id }) { objeto ->
                TarjetaSolicitud(
                    objeto = objeto,
                    onAceptar = { onEvent(ObjetoEvent.OnAceptar(objeto.id)) },
                    onRechazar = { onEvent(ObjetoEvent.OnRechazar(objeto.id)) },
                    onVerQr = { onVerQr(objeto.id) }
                )
            }
        }
    }

}
@Composable
private fun TarjetaSolicitud(
    objeto: Objeto,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit,
    onVerQr: () -> Unit
) {
    val pendiente = objeto.estado == Opciones.SOLICITADO
    val forma = RoundedCornerShape(24.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, forma)
            .border(1.dp, Borde, forma)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(colorCategoria(objeto.categoria), RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = objeto.nombre.take(1).uppercase(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = objeto.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoOscuro,
                        modifier = Modifier.weight(1f)
                    )
                    Etiqueta(if (pendiente) "Pendiente" else "Aceptada", destacada = !pendiente)
                }
                Text(
                    text = buildAnnotatedString {
                        append("Pide: ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextoOscuro)) {
                            append(objeto.solicitante)
                        }
                    },
                    color = TextoGris
                )
                Text(
                    text = if (pendiente) {
                        "${Fechas.haceCuanto(objeto.fechaSolicitado)} · ${objeto.modalidad}"
                    } else {
                        "Acuerdo confirmado · QR generado"
                    },
                    fontSize = 14.sp,
                    color = TextoGris
                )
            }
        }

        HorizontalDivider(color = Borde)

        if (pendiente) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BotonSecundario("Rechazar", onRechazar, Modifier.weight(1f))
                BotonPrincipal("Aceptar", onAceptar, Modifier.weight(1f))
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Entrega programada", color = TextoGris)
                BotonSecundario("Ver código QR", onVerQr, Modifier.width(170.dp))
            }
        }
    }
}
package com.reeloop.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.presentation.common.colorCategoria
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdePrincipal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PasaporteScreen(objeto: Objeto?, onVolver: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { EncabezadoAtras("Pasaporte circular", onVolver) }
        item {
            Text("Pasaporte del objeto", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Text("Historia local de esta nueva vida.", color = TextoGris)
        }
        if (objeto == null) {
            item { Text("No se encontró el objeto.", color = TextoGris) }
            return@LazyColumn
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth().background(VerdeClaro.copy(alpha = 0.45f), RoundedCornerShape(22.dp)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(64.dp).background(colorCategoria(objeto.categoria), RoundedCornerShape(16.dp))
                ) {
                    Text(objeto.nombre.take(1).uppercase(), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(objeto.nombre, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                    Text("Objeto #RL-${objeto.id.toString().padStart(4, '0')} · ${objeto.modalidad.uppercase()}", color = TextoGris)
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                PasoPasaporte("PUBLICADO", "Objeto registrado en Room", objeto.fechaPublicado, true)
                if (objeto.fechaSolicitado > 0L) {
                    PasoPasaporte("SOLICITADO", "Solicitud aceptada o pendiente", objeto.fechaSolicitado, true)
                }
                if (objeto.fechaEntregado > 0L || objeto.estado == Opciones.ENTREGADO) {
                    PasoPasaporte("ENTREGADO", "Entrega marcada como completada", objeto.fechaEntregado, false)
                }
            }
        }
    }
}

@Composable
private fun PasoPasaporte(titulo: String, detalle: String, fecha: Long, mostrarLinea: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(22.dp).background(VerdePrincipal, CircleShape))
            if (mostrarLinea) {
                Box(Modifier.width(2.dp).fillMaxHeight().background(Borde))
            }
        }
        Column(modifier = Modifier.padding(bottom = 28.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold, color = TextoOscuro, letterSpacing = 1.sp)
            Text(detalle, color = TextoGris)
            Text(formatearFecha(fecha), color = TextoGris, fontSize = 13.sp)
        }
    }
}

private fun formatearFecha(valor: Long): String {
    if (valor <= 0L) return "Sin fecha"
    return SimpleDateFormat("dd MMM yyyy · HH:mm", Locale.forLanguageTag("es-PE")).format(Date(valor))
}

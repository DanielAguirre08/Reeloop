package com.reeloop.app.presentation.screens.detalle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.EncabezadoAtras
import com.reeloop.app.presentation.common.Etiqueta
import com.reeloop.app.presentation.common.colorCategoria
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun DetalleScreen(
    objeto: Objeto?,
    onVolver: () -> Unit,
    onSolicitar: () -> Unit,
    onPasaporte: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EncabezadoAtras("Detalle del objeto", onVolver)

        if (objeto == null) {
            Text("Este objeto ya no existe.", color = TextoGris)
            return@Column
        }
        val disponible = objeto.estado == Opciones.DISPONIBLE

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(colorCategoria(objeto.categoria), RoundedCornerShape(24.dp))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp).background(Color.White.copy(alpha = 0.45f), CircleShape)
            ) {
                Text(
                    text = objeto.nombre.take(1).uppercase(),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = objeto.nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro,
                modifier = Modifier.weight(1f)
            )
            Etiqueta(objeto.estado, destacada = disponible)
        }

        Text("${objeto.categoria} · ${objeto.conservacion}", color = TextoGris)
        Text(objeto.descripcion, color = TextoOscuro, lineHeight = 24.sp)
        Text("Peso aproximado: ${objeto.peso} kg", color = TextoGris)

        Column {
            Text("MODALIDAD", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextoGris, letterSpacing = 1.sp)
            Text(objeto.modalidad.uppercase(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VerdePrincipal)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotonSecundario("Ver pasaporte", onPasaporte, Modifier.weight(1f))
            BotonPrincipal(
                texto = if (disponible) "Solicitar objeto" else "Ya solicitado",
                onClick = onSolicitar,
                modifier = Modifier.weight(1f),
                habilitado = disponible
            )
        }
    }
}

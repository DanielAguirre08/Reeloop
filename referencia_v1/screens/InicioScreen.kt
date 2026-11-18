package com.reeloop.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.AvatarUsuarios
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.LogoReeloop
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun InicioScreen(
    state: ObjetoState,
    onPublicar: () -> Unit,
    onExplorar: () -> Unit,
    onMisObjetos: () -> Unit,
    onSolicitudes: () -> Unit,
    onEscanear: () -> Unit
) {
    val reutilizados = state.listaObjeto.count { it.estado == Opciones.ENTREGADO }
    val solicitudes = state.listaObjeto.count {
        it.estado == Opciones.SOLICITADO || it.estado == Opciones.ACEPTADO
    }

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LogoReeloop(tamaño = 34.dp)
                Text(
                    "ReLoop",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextoOscuro,
                    modifier = Modifier.padding(start = 10.dp).weight(1f)
                )
                AvatarUsuarios()
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Hola, Ana", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Text("¿Qué objeto puede tener una nueva vida hoy?", color = TextoGris)
            }
        }
        item { TarjetaImpacto(reutilizados, state.listaObjeto.size) }
        item {
            Text("Acciones principales", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BotonPrincipal("Tengo un objeto", onPublicar, Modifier.weight(1f))
                BotonSecundario("Necesito uno", onExplorar, Modifier.weight(1f))
            }
        }
        item {
            Text("Accesos rápidos", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AccesoRapido(
                    icono = Icons.Default.Inventory2,
                    titulo = "Mis objetos",
                    detalle = "${state.listaObjeto.size} publicaciones",
                    onClick = onMisObjetos
                )
                AccesoRapido(
                    icono = Icons.AutoMirrored.Filled.ListAlt,
                    titulo = "Solicitudes",
                    detalle = "$solicitudes pendientes o aceptadas",
                    onClick = onSolicitudes
                )
                AccesoRapido(
                    icono = Icons.Default.QrCodeScanner,
                    titulo = "Escanear entrega",
                    detalle = "Vista de demostración sin cámara",
                    onClick = onEscanear
                )
            }
        }
    }
}

@Composable
private fun TarjetaImpacto(reutilizados: Int, total: Int) {
    val progreso = if (total == 0) 0f else reutilizados.toFloat() / total.toFloat()
    Column(
        modifier = Modifier.fillMaxWidth().background(VerdePrincipal, RoundedCornerShape(24.dp)).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Tu EcoImpacto", color = Color.White, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("$reutilizados", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
            Text("objetos reutilizados", color = Color.White.copy(alpha = 0.9f), modifier = Modifier.padding(bottom = 6.dp))
        }
        LinearProgressIndicator(
            progress = { progreso },
            color = VerdeClaro,
            trackColor = VerdeOscuro.copy(alpha = 0.45f),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}

@Composable
private fun AccesoRapido(icono: ImageVector, titulo: String, detalle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp).background(VerdeClaro, CircleShape)
        ) {
            Icon(icono, contentDescription = null, tint = VerdePrincipal)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
            Text(detalle, color = TextoGris, fontSize = 13.sp)
        }
        Text("›", color = Borde, fontSize = 28.sp)
    }
}

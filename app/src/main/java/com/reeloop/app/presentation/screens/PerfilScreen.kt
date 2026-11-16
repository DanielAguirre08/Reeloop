package com.reeloop.app.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.reeloop.app.presentation.common.Encabezado
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.Rojo
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun PerfilScreen(
    state: ObjetoState,
    onMisObjetos: () -> Unit,
    onSolicitudes: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val solicitudes = state.listaObjeto.count {
        it.estado == Opciones.SOLICITADO || it.estado == Opciones.ACEPTADO
    }

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { Encabezado("Perfil") }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(24.dp)).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(92.dp).background(VerdeClaro, CircleShape)
                ) {
                    Text("A", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = VerdeOscuro)
                }
                Text("Ana Torres", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Text("ana@correo.com", color = TextoGris)
            }
        }
        item {
            Text("TUS ACTIVIDADES:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextoGris, letterSpacing = 1.sp)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OpcionPerfil(
                    icono = Icons.Default.Inventory2,
                    titulo = "Mis objetos",
                    detalle = "${state.listaObjeto.size} publicaciones",
                    onClick = onMisObjetos
                )
                OpcionPerfil(
                    icono = Icons.AutoMirrored.Filled.ListAlt,
                    titulo = "Solicitudes",
                    detalle = "$solicitudes activas",
                    onClick = onSolicitudes
                )
            }
        }
        item {
            OutlinedButton(
                onClick = onCerrarSesion,
                border = BorderStroke(1.5.dp, Rojo),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", color = Rojo, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun OpcionPerfil(
    icono: ImageVector,
    titulo: String,
    detalle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(18.dp)).clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp).background(VerdeClaro, RoundedCornerShape(14.dp))
        ) {
            Icon(icono, contentDescription = null, tint = VerdeOscuro)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
            Text(detalle, color = TextoGris, fontSize = 13.sp)
        }
        Text("›", color = TextoGris, fontSize = 28.sp)
    }
}

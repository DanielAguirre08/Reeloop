package com.reeloop.app.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import android.content.Intent
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.navigation.Rutas
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun ApptopBar(navController: NavController, sesion : SesionRepository){
    val entrada by navController.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    if (ruta == null || ruta in Rutas.sinBarras) return

    val usuario by sesion.usuario.collectAsState()
    val inicial = usuario?.nombre?.firstOrNull()?.uppercase() ?: "R"
    var menuAbierto by remember { mutableStateOf(false) }
    var mostrarAcercaDe by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (ruta in Rutas.conFlechaAtras) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextoOscuro)
            }
        } else {
            Spacer(Modifier.width(8.dp))
            LogoReeloop(tamaño = 32.dp)
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = Rutas.getTitulo(ruta),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.size(32.dp).background(VerdePrincipal, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(inicial, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Box {
            IconButton(onClick = { menuAbierto = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Más opciones", tint = TextoOscuro)
            }
            DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                DropdownMenuItem(
                    text = { Text("Mi impacto") },
                    onClick = {
                        menuAbierto = false
                        navController.navigate(Rutas.IMPACTO) {
                            popUpTo(Rutas.INICIO) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Compartir ReLoop") },
                    onClick = {
                        menuAbierto = false
                        val envio = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Dale una segunda vida a tus objetos con ReLoop ♻️")
                        }
                        context.startActivity(Intent.createChooser(envio, "Compartir ReLoop"))
                    }
                )
                DropdownMenuItem(
                    text = { Text("Acerca de") },
                    onClick = { menuAbierto = false; mostrarAcercaDe = true }
                )
                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = {
                        menuAbierto = false
                        sesion.cerrar()
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }

    if (mostrarAcercaDe) {
        AlertDialog(
            onDismissRequest = { mostrarAcercaDe = false },
            confirmButton = { TextButton(onClick = { mostrarAcercaDe = false }) { Text("Cerrar") } },
            title = { Text("ReLoop") },
            text = {
                Text(
                    "App de economía circular (ODS 12) para la comunidad universitaria.\n\n" +
                            "Grupo 05 · UPN\nIntegrantes: Daniel, Cesar, Erik y Joel"
                )
            }
        )
    }
}
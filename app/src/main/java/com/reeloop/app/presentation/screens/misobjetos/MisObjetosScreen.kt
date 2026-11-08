package com.reeloop.app.presentation.screens.misobjetos
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.Encabezado
import com.reeloop.app.presentation.common.ObjetoItem
import com.reeloop.app.presentation.event.ObjetoEvent
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.GrisClaro
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun MisObjetosScreen(
    state: ObjetoState,
    onEvent: (ObjetoEvent) -> Unit,
    onPublicar: () -> Unit,
    onVerDetalle: (Int) -> Unit
) {
    // Al salir de la pantalla, el banner de éxito se apaga
    DisposableEffect(Unit) {
        onDispose { onEvent(ObjetoEvent.OnExitoVisto) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Encabezado("Mis objetos") }

        if (state.exito) {
            item { BannerExito() }
        }

        if (state.listaObjeto.isEmpty()) {
            item {
                Text(
                    text = "Aún no has publicado objetos.",
                    color = TextoGris,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                )
            }
            item { BotonPrincipal("Publicar objeto", onClick = onPublicar) }
        } else {
            items(state.listaObjeto, key = { it.id }) { objeto ->
                ObjetoItem(objeto = objeto, mostrarFecha = true, onClick = { onVerDetalle(objeto.id) })
            }
        }

        item {
            Text(
                text = "Consejo: una buena foto aumenta las solicitudes.",
                color = TextoGris,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GrisClaro, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun BannerExito() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(VerdeClaro, RoundedCornerShape(20.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(44.dp).background(VerdeOscuro, CircleShape)
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
        }
        Column {
            Text("¡Publicación creada!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Text("Tu objeto ya aparece en Explorar.", color = TextoGris)
        }
    }
}

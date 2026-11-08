package com.reeloop.app.presentation.screens.publicar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.CampoSelector
import com.reeloop.app.presentation.common.CampoTexto
import com.reeloop.app.presentation.common.ChipOpcion
import com.reeloop.app.presentation.common.Encabezado
import com.reeloop.app.presentation.event.ObjetoEvent
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.Rojo
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun PublicarScreen(
    state: ObjetoState,
    onEvent: (ObjetoEvent) -> Unit,
    onPublicado: () -> Unit
){
    LaunchedEffect(state.publicado) {
        if(state.publicado){
            onEvent(ObjetoEvent.OnPublicadoConsumido)
            onPublicado()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado("Publicar objeto")
        ZonaFoto()

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CampoTexto(
                valor = state.nombre,
                onCambio = { onEvent(ObjetoEvent.OnNombreChange(it)) },
                etiqueta = "Nombre",
                modifier = Modifier.weight(1f)
            )
            CampoSelector(
                valor = state.categoria,
                opciones = Opciones.categorias,
                etiqueta = "Categoría",
                onSeleccion = { onEvent(ObjetoEvent.OnCategoriaChange(it)) },
                modifier = Modifier.weight(1f)
            )
        }

        CampoTexto(
            valor = state.descripcion,
            onCambio = { onEvent(ObjetoEvent.OnDescripcionChange(it)) },
            etiqueta = "Descripción",
            lineas = 3
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CampoSelector(
                valor = state.conservacion,
                opciones = Opciones.conservaciones,
                etiqueta = "Conservación",
                onSeleccion = { onEvent(ObjetoEvent.OnConservacionChange(it)) },
                modifier = Modifier.weight(1f)
            )
            CampoTexto(
                valor = state.peso,
                onCambio = { onEvent(ObjetoEvent.OnPesoChange(it)) },
                etiqueta = "Peso (kg)",
                modifier = Modifier.weight(1f),
                teclado = KeyboardType.Decimal
            )
        }

        Text("Modalidad", fontWeight = FontWeight.Bold, color = TextoOscuro)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Opciones.modalidades.forEach { modalidad ->
                ChipOpcion(
                    texto = modalidad,
                    seleccionado = state.modalidad == modalidad,
                    onClick = { onEvent(ObjetoEvent.OnModalidadChange(modalidad)) }
                )
            }
        }

        if (state.error.isNotEmpty()) {
            Text(state.error, color = Rojo)
        }
        BotonPrincipal("Publicar objeto", onClick = { onEvent(ObjetoEvent.OnPublicar) })
    }
}
@Composable
private fun ZonaFoto() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(VerdeClaro.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .border(1.dp, Borde, RoundedCornerShape(20.dp))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp).background(VerdeClaro, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = VerdePrincipal)
            }
            Text("Añadir fotografía", color = VerdePrincipal, fontWeight = FontWeight.SemiBold)
        }
    }
}




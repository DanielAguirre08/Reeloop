package com.reeloop.app.presentation.screens.explorar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.CampoTexto
import com.reeloop.app.presentation.common.ChipOpcion
import com.reeloop.app.presentation.common.Encabezado
import com.reeloop.app.presentation.common.ObjetoItem
import com.reeloop.app.presentation.event.ObjetoEvent
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro

@Composable
fun ExplorarScreen(
    state: ObjetoState,
    onEvent: (ObjetoEvent) -> Unit,
    onVerDetalle: (Int) -> Unit
){


    val filtros= listOf("Todos") + Opciones.modalidades

    val resultados = state.listaObjeto .filter {
        objeto -> objeto.estado == Opciones.DISPONIBLE &&
            (state.filtroModalidad =="Todos" || objeto.modalidad == state.filtroModalidad &&
                    objeto.nombre.contains(state.busqueda, ignoreCase = true))
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Encabezado("Explorar") }
        item {
            CampoTexto(
                valor = state.busqueda,
                onCambio = { onEvent(ObjetoEvent.OnBusquedaChange(it)) },
                etiqueta = "Buscar por nombre",
                icono = Icons.Default.Search
            )
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
            ) {
                filtros.forEach { filtro ->
                    ChipOpcion(
                        texto = filtro,
                        seleccionado = state.filtroModalidad == filtro,
                        onClick = { onEvent(ObjetoEvent.OnFiltroModalidad(filtro)) }
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Resultados destacados", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
                Text("${resultados.size} objetos", color = TextoGris)
            }
        }

        if (resultados.isEmpty()) {
            item {
                Text(
                    text = "No hay objetos para mostrar.",
                    color = TextoGris,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                )
            }
        } else {
            items(resultados, key = { it.id }) { objeto ->
                ObjetoItem(objeto = objeto, onClick = { onVerDetalle(objeto.id) })
            }
        }
    }

}
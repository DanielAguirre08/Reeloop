package com.reeloop.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Opciones
import com.reeloop.app.presentation.common.Encabezado
import com.reeloop.app.presentation.viewmodel.ObjetoState
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun ImpactoScreen(state: ObjetoState) {
    val completados = state.listaObjeto.filter { it.estado == Opciones.ENTREGADO }
    val donaciones = completados.count { it.modalidad == Opciones.DONACION }
    val prestamos = completados.count { it.modalidad == Opciones.PRESTAMO }
    val intercambios = completados.count { it.modalidad == Opciones.INTERCAMBIO }

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Encabezado("EcoImpacto") }
        item {
            Text("Tu EcoImpacto", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Metrica(completados.size, "Reutilizados", Modifier.weight(1f))
                Metrica(donaciones, "Donaciones", Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Metrica(prestamos, "Préstamos", Modifier.weight(1f))
                Metrica(intercambios, "Intercambios", Modifier.weight(1f))
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().background(VerdeOscuro, RoundedCornerShape(24.dp)).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Ciclos completados", color = VerdeClaro, fontWeight = FontWeight.SemiBold)
                Text("${completados.size}", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Bold)
                Text("objetos ya tuvieron otra vida", color = Color.White)
            }
        }
    }
}

@Composable
private fun Metrica(valor: Int, titulo: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(VerdeClaro.copy(alpha = 0.6f), RoundedCornerShape(20.dp)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("$valor", color = VerdePrincipal, fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Text(titulo, color = TextoOscuro)
    }
}

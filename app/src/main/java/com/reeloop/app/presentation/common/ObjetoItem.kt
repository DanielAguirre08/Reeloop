package com.reeloop.app.presentation.common
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.ui.theme.AzulClaro
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.Crema
import com.reeloop.app.ui.theme.Durazno
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.util.Fechas

// Cada categoría siempre recibe el mismo color de la paleta
fun colorCategoria(categoria: String): Color {
    val paleta = listOf(Crema, VerdeClaro, AzulClaro, Durazno)
    return paleta[(categoria.hashCode() and Int.MAX_VALUE) % paleta.size]
}

@Composable
fun ObjetoItem(
    objeto: Objeto,
    modifier: Modifier = Modifier,
    mostrarFecha: Boolean = false,
    onClick: () -> Unit = {}
) {
    val forma = RoundedCornerShape(20.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.White)
            .border(1.dp, Borde, forma)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(72.dp)
                .background(colorCategoria(objeto.categoria), RoundedCornerShape(16.dp))
        ) {
            Text(
                text = objeto.nombre.take(1).uppercase(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(objeto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
            Text("${objeto.categoria} · ${objeto.conservacion}", fontSize = 14.sp, color = TextoGris)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Etiqueta(objeto.modalidad, destacada = true)
                Etiqueta(objeto.estado)
            }
            if (mostrarFecha) {
                Text("Publicada ${Fechas.haceCuanto(objeto.fechaPublicado)}", fontSize = 12.sp, color = TextoGris)
            }
        }
    }
}

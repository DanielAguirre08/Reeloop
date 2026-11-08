package com.reeloop.app.presentation.common
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reeloop.app.ui.theme.Borde
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeClaro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun CampoSelector(
    valor: String,
    opciones: List<String>,
    etiqueta: String,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var abierto by remember { mutableStateOf(false) }

    Box(modifier) {
        CampoTexto(valor = valor, onCambio = {}, etiqueta = etiqueta, readOnly = true)
        // Capa transparente encima: captura el toque y abre el menú
        Box(Modifier.matchParentSize().clickable { abierto = true })

        DropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Borde)
        ) {
            opciones.forEach { opcion ->
                val elegida = opcion == valor
                DropdownMenuItem(
                    text = {
                        Text(
                            text = opcion,
                            color = if (elegida) VerdeOscuro else TextoOscuro,
                            fontWeight = if (elegida) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onSeleccion(opcion)
                        abierto = false
                    },
                    modifier = Modifier.background(if (elegida) VerdeClaro else Color.Transparent)
                )
            }
        }
    }
}



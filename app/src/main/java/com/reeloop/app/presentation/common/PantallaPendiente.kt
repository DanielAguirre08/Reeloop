package com.reeloop.app.presentation.common

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro

@Composable
fun PantallaPendiente(titulo: String, modulo: String, modifier: Modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Text("En construcción · $modulo", fontSize = 14.sp, color = TextoGris)
    }
}
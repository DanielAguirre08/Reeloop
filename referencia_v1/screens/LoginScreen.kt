package com.reeloop.app.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.CampoTexto
import com.reeloop.app.presentation.common.LogoReeloop
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeOscuro

@Composable
fun LoginScreen(
    onEntrar: () -> Unit,
    onCrearCuenta: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LogoReeloop()
            Text("ReLoop", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = VerdeOscuro)
        }
        Text("Da una segunda vida a objetos útiles", color = TextoGris)

        Spacer(Modifier.height(28.dp))
        Text("Bienvenido de nuevo", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Spacer(Modifier.height(1.dp))
        CampoTexto(
            valor = correo,
            onCambio = { correo = it },
            etiqueta = "Correo electrónico",
            teclado = KeyboardType.Email
        )
        CampoTexto(
            valor = clave,
            onCambio = { clave = it },
            etiqueta = "Contraseña",
            esPassword = true
        )

        BotonPrincipal(texto = "Entrar", onClick = onEntrar)

        Spacer(Modifier.weight(1f))
        Text(
            "¿Aún no tienes cuenta?",
            color = TextoGris,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        BotonSecundario("Crear cuenta", onCrearCuenta, Modifier.fillMaxWidth())
    }
}

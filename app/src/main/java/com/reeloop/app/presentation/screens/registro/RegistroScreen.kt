package com.reeloop.app.presentation.screens.registro

import android.R
import android.text.Layout
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.CampoTexto
import com.reeloop.app.presentation.common.LogoReeloop
import com.reeloop.app.ui.theme.Rojo
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun RegistroScreen(
    onRegistrarme: () ->Unit,
    onYatengoCuenta: () ->Unit
){
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var acepta by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            LogoReeloop( tamaño = 40.dp)
            Text("ReLoop", fontSize =  26.sp, fontWeight = FontWeight.Bold, color = VerdeOscuro)
        }
        Spacer(Modifier.height(32.dp))
        Text("Crear tu cuenta", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Text("Unete a una comunidad que comparte y reutiliza", color = TextoGris)
        Spacer(Modifier.height(24.dp))
        CampoTexto(
            valor = nombre ,
            onCambio = {nombre=it; error=""},
            etiqueta = "Nombre Completo",
        )

        Spacer(Modifier.height(12.dp))

        CampoTexto(
            valor = correo,
            onCambio = {correo=it; error=""},
            etiqueta = "Correo electronico",
            teclado = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CampoTexto(
                valor = clave,
                onCambio = { clave = it; error = "" },
                etiqueta = "Contraseña",
                modifier = Modifier.weight(1f),
                esPassword = true
            )
            CampoTexto(
                valor = confirmar,
                onCambio = { confirmar = it; error = "" },
                etiqueta = "Confirmar",
                modifier = Modifier.weight(1f),
                esPassword = true
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = acepta,
                onCheckedChange = {acepta=it ;error= ""},
                colors = CheckboxDefaults.colors(checkedColor = VerdePrincipal)
            )
            Text("Acepto los terminos y el uso responsable de ReLoop", color = TextoGris , fontSize = 14.sp)

        }
        if (error.isNotEmpty()){
            Text(error, color= Rojo, modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(Modifier.height(8.dp))
        BotonPrincipal(
            texto = "Registrarme",
            onClick = {
                error = when {
                    nombre.isBlank() || correo.isBlank() || clave.isBlank() -> "Completa todos los campos"
                    clave != confirmar -> "Las contraseñas no coinciden"
                    !acepta -> "Debes aceptar los términos"
                    else -> ""
                }
                if (error.isEmpty()) onRegistrarme()
            }
        )
        TextButton(
            onClick = onYatengoCuenta,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Ya tengo una cuenta", color = VerdePrincipal, fontWeight = FontWeight.SemiBold)
        }


    }


}
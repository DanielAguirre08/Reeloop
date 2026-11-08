package com.reeloop.app.presentation.screens.login

import android.R
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.reeloop.app.presentation.common.BotonPrincipal
import com.reeloop.app.presentation.common.BotonSecundario
import com.reeloop.app.presentation.common.CampoTexto
import com.reeloop.app.presentation.common.LogoReeloop
import com.reeloop.app.ui.theme.Rojo
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.TextoOscuro
import com.reeloop.app.ui.theme.VerdeOscuro
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun LoginScreen(
    onEntrar:() -> Unit,
    onCrearCuenta: () -> Unit
){
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var hayError by remember { mutableStateOf(false) }

 Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
     Row(
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.spacedBy(12.dp)
     ) {
         LogoReeloop()
         Text("ReLoop", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = VerdeOscuro)
     }
     Text("Da una segunda vida a objetos ùtiles ", color = TextoGris)

     Spacer(Modifier.height(40.dp))
     Text("Bienvenido de nuevo", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
    Text("Ingresa para continuar reutilizando", color = TextoGris)

     Spacer(Modifier.height(24.dp))
     CampoTexto(
         valor = correo,
         onCambio = {correo=it; hayError=false},
         etiqueta = "Correo electronico",
         teclado = KeyboardType.Email
     )
     Spacer(Modifier.height(12.dp))
     CampoTexto(
         valor = clave,
         onCambio = { clave = it; hayError = false },
         etiqueta = "Contraseña",
         esPassword = true
     )
     Text(
         text = "¿Olvidaste tu contraseña?",
         color = VerdePrincipal,
         fontWeight = FontWeight.SemiBold,
         modifier = Modifier.align(Alignment.End).padding(vertical = 12.dp)
     )

     if(hayError) {
         Text("Completa tu correo y contraseña", color = Rojo , modifier = Modifier.padding(bottom = 8.dp))
     }
     BotonPrincipal(
         texto = "Entrar",
         onClick = {if (correo.isBlank()|| clave.isBlank()) hayError =true else onEntrar()}
     )

     Spacer(Modifier.weight(1f))
     Text("¿Aún no tienes cuenta?", color = TextoGris, modifier = Modifier.align(Alignment.CenterHorizontally))
     Spacer(Modifier.height(8.dp))
     BotonSecundario("Crear cuenta", onCrearCuenta)

 }


}
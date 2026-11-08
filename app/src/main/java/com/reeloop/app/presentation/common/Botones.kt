package com.reeloop.app.presentation.common

import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun BotonPrincipal(
    texto : String,
    onClick : () -> Unit,
    modifier:  Modifier = Modifier ,
    habilitado: Boolean = true

)
{
   Button(
        onClick = onClick,
       enabled = habilitado,
       shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = VerdePrincipal,
            contentColor = Color.White,
            disabledContainerColor = VerdePrincipal.copy(alpha = 0.4f),
            disabledContentColor = Color.White
        ),
        modifier = modifier.fillMaxWidth().height(56.dp)

    ){
        Text(texto, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun BotonSecundario (texto: String , onClick: () -> Unit, modifier: Modifier = Modifier){
    OutlinedButton(
        onClick,
        shape = CircleShape,
        border = BorderStroke(1.5.dp, VerdePrincipal),
        colors = ButtonDefaults.outlinedButtonColors(Color.White),
        modifier = modifier.fillMaxWidth().height(56.dp)

    ) {
        Text(texto, fontWeight = FontWeight.SemiBold)
    }

}
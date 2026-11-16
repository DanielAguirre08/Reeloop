package com.reeloop.app.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.reeloop.app.ui.theme.VerdePrincipal

@Composable
fun LogoReeloop(tamaño: Dp =56.dp  ){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(tamaño).border(2.dp, VerdePrincipal, CircleShape)
    ){
        Icon(
            imageVector = Icons.Outlined.Inventory2,
            contentDescription = "LogoReloop",
            tint = VerdePrincipal,
            modifier = Modifier.size(tamaño * 0.5f)
        )

    }

}
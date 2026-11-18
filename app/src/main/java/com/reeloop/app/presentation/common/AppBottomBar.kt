package com.reeloop.app.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.currentBackStackEntryAsState
import com.reeloop.app.navigation.Rutas
import com.reeloop.app.ui.theme.TextoGris
import com.reeloop.app.ui.theme.VerdePrincipal
import org.w3c.dom.Text

private data class ItemBarra(val ruta: String, val titulo: String, val icono: ImageVector)

private val items = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Default.Home),
    ItemBarra(Rutas.EXPLORAR, "Explorar", Icons.Default.Search),
    ItemBarra(Rutas.PUBLICAR, "Publicar", Icons.Default.AddCircleOutline),
    ItemBarra(Rutas.IMPACTO, "Impacto", Icons.Default.Eco),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

@Composable
fun AppBottomBar(navController: NavHostController) {
    val entrada by navController.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    if (ruta == null || ruta in Rutas.sinBarras || ruta in Rutas.sinBarraInferior) return

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            NavigationBarItem(
                selected = Rutas.pestanaDe(ruta) == item.ruta,
                onClick = {
                    navController.navigate(item.ruta) {
                        popUpTo(Rutas.INICIO) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icono, contentDescription = item.titulo) },
                label = { Text(item.titulo) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = VerdePrincipal,
                    indicatorColor = VerdePrincipal,
                    unselectedIconColor = TextoGris,
                    unselectedTextColor = TextoGris
                )
            )
        }
    }
}




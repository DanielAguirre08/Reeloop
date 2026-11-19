package com.reeloop.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiMode
import androidx.navigation.compose.rememberNavController
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.navigation.AppNavigation
import com.reeloop.app.presentation.common.AppScaffold
import com.reeloop.app.presentation.common.SnackbarTipo
import com.reeloop.app.presentation.event.UiEventBus
import com.reeloop.app.presentation.event.UiEvent
import com.reeloop.app.ui.theme.ReeloopTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var eventBus: UiEventBus
    @Inject lateinit var sesion: SesionRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReeloopTheme {
                val navController = rememberNavController()


                AppScaffold(navController, eventBus, sesion) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        AppNavigation(navController, sesion)
                    }
                }
            }
        }
        }
    }


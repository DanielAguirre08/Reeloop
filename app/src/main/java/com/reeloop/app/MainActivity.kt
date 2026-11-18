package com.reeloop.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.AndroidUiMode
import com.reeloop.app.domain.repository.SesionRepository
import com.reeloop.app.navigation.AppNavigation
import com.reeloop.app.presentation.event.UIeventBus
import com.reeloop.app.ui.theme.ReeloopTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var eventBus: UIeventBus
    @Inject lateinit var sesion: SesionRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReeloopTheme {
                AppNavigation()
            }
        }
    }
}

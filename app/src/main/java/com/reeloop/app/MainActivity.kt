package com.reeloop.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.AndroidUiMode
import com.reeloop.app.navigation.AppNavigation
import com.reeloop.app.ui.theme.ReeloopTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

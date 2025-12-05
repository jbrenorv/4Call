package com.jbrenorv.acall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jbrenorv.acall.core.designsystem.theme.ACallTheme
import com.jbrenorv.acall.navigation.ACallNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACallTheme {
                ACallNavHost()
            }
        }
    }
}

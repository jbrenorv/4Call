package com.jbrenorv.a4call

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jbrenorv.a4call.navigation.A4CallNavHost
import com.jbrenorv.a4call.ui.theme.A4CallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A4CallTheme {
                A4CallNavHost()
            }
        }
    }
}

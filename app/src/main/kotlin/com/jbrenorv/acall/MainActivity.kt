package com.jbrenorv.acall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.designsystem.theme.ACallTheme
import com.jbrenorv.acall.ui.ACallApp
import com.jbrenorv.acall.ui.rememberACallAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val appState = rememberACallAppState(
                authRepository = authRepository
            )

            ACallTheme {
                ACallApp(
                    appState = appState
                )
            }
        }
    }
}

package com.jbrenorv.acall.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.acall.feature.login.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
data class LoginRoute(
    val webClientId: String
)

fun NavGraphBuilder.loginScreen() {
    composable<LoginRoute> {
        LoginRoute()
    }
}

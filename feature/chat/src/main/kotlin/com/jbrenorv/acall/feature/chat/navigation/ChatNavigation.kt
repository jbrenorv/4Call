package com.jbrenorv.acall.feature.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jbrenorv.acall.feature.chat.ChatScreen
import kotlinx.serialization.Serializable

@Serializable
data object ChatRoute

fun NavHostController.openChatScreen() = navigate(ChatRoute)

fun NavGraphBuilder.chatScreen(
    goBack: () -> Unit,
) {
    composable<ChatRoute> {
        ChatScreen(
            goBack = goBack
        )
    }
}

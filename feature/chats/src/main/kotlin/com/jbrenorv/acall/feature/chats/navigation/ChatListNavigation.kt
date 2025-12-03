package com.jbrenorv.acall.feature.chats.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.acall.feature.chats.ChatListScreen
import kotlinx.serialization.Serializable

@Serializable
data object ChatListRoute

fun NavGraphBuilder.chatListScreen(
    openChat: () -> Unit
) {
    composable<ChatListRoute> {
        ChatListScreen(
            openChat = openChat
        )
    }
}

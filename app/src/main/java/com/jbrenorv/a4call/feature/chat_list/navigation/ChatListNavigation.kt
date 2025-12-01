package com.jbrenorv.a4call.feature.chat_list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.a4call.feature.chat_list.ChatListScreen
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

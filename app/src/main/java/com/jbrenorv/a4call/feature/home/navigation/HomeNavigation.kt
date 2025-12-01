package com.jbrenorv.a4call.feature.home.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.a4call.feature.chat_list.navigation.ChatListRoute
import com.jbrenorv.a4call.feature.home.HomeScreen
import com.jbrenorv.a4call.feature.room_list.navigation.RoomListRoute
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

sealed class HomeNavigationBarDestination(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val iconContentDescription: String
) {
    object RoomListDestination : HomeNavigationBarDestination(
        route = RoomListRoute,
        label = "Rooms",
        icon = Icons.Filled.Home,
        iconContentDescription = "Room list screen"
    )

    object ChatListDestination : HomeNavigationBarDestination(
        route = ChatListRoute,
        label = "Chats",
        icon = Icons.AutoMirrored.Filled.Chat,
        iconContentDescription = "Chat list screen"
    )
}

fun NavGraphBuilder.homeScreen(
    openRoom: () -> Unit,
    openChat: () -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(
            openRoom = openRoom,
            openChat = openChat,
            startDestination = HomeNavigationBarDestination.RoomListDestination,
            destinations = listOf(
                HomeNavigationBarDestination.RoomListDestination,
                HomeNavigationBarDestination.ChatListDestination
            )
        )
    }
}

package com.jbrenorv.acall.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.acall.HomeScreen
import com.jbrenorv.acall.core.designsystem.icon.ACallIcons
import com.jbrenorv.acall.feature.chats.navigation.ChatListRoute
import com.jbrenorv.acall.feature.rooms.navigation.RoomListRoute
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
        icon = ACallIcons.Home,
        iconContentDescription = "Room list screen"
    )

    object ChatListDestination : HomeNavigationBarDestination(
        route = ChatListRoute,
        label = "Chats",
        icon = ACallIcons.Chat,
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

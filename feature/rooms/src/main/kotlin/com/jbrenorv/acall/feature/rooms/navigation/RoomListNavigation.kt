package com.jbrenorv.acall.feature.rooms.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.acall.feature.rooms.RoomListRoute
import kotlinx.serialization.Serializable

@Serializable
data object RoomListRoute

fun NavGraphBuilder.roomListScreen(
    openRoom: () -> Unit
) {
    composable<RoomListRoute> {
        RoomListRoute(
            openRoom = openRoom
        )
    }
}

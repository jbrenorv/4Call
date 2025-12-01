package com.jbrenorv.a4call.feature.room_list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jbrenorv.a4call.feature.room_list.RoomListScreen
import kotlinx.serialization.Serializable

@Serializable
data object RoomListRoute

fun NavGraphBuilder.roomListScreen(
    openRoom: () -> Unit
) {
    composable<RoomListRoute> {
        RoomListScreen(
            openRoom = openRoom
        )
    }
}

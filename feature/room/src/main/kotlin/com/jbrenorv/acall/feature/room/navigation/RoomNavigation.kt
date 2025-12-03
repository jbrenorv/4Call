package com.jbrenorv.acall.feature.room.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jbrenorv.acall.feature.room.RoomScreen
import kotlinx.serialization.Serializable

@Serializable
data object RoomRoute

fun NavHostController.openRoomScreen() = navigate(RoomRoute)

fun NavGraphBuilder.roomScreen(
    goBack: () -> Unit,
) {
    composable<RoomRoute> {
        RoomScreen(
            goBack = goBack
        )
    }
}

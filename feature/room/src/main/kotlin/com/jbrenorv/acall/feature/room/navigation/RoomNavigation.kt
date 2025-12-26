package com.jbrenorv.acall.feature.room.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.jbrenorv.acall.core.common.deeplink.ACallDeepLinks
import com.jbrenorv.acall.feature.room.RoomScreen
import kotlinx.serialization.Serializable

@Serializable
data object RoomRoute

fun NavHostController.openRoomScreen() = navigate(RoomRoute)

fun NavGraphBuilder.roomScreen(
    goBack: () -> Unit,
) {
    composable<RoomRoute>(
        deepLinks = listOf(
            navDeepLink {
                uriPattern = ACallDeepLinks.ROOM
            }
        )
    ) {
        RoomScreen(
            goBack = goBack
        )
    }
}

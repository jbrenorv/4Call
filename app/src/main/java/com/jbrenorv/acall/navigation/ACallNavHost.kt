package com.jbrenorv.acall.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jbrenorv.acall.feature.chat.navigation.chatScreen
import com.jbrenorv.acall.feature.chat.navigation.openChatScreen
import com.jbrenorv.acall.feature.room.navigation.openRoomScreen
import com.jbrenorv.acall.feature.room.navigation.roomScreen

@Composable
fun ACallNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        homeScreen(
            openRoom = navController::openRoomScreen,
            openChat = navController::openChatScreen
        )

        roomScreen(
            goBack = navController::popBackStack
        )

        chatScreen(
            goBack = navController::popBackStack
        )
    }
}

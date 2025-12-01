package com.jbrenorv.a4call.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jbrenorv.a4call.feature.chat.navigation.chatScreen
import com.jbrenorv.a4call.feature.chat.navigation.openChatScreen
import com.jbrenorv.a4call.feature.home.navigation.HomeRoute
import com.jbrenorv.a4call.feature.home.navigation.homeScreen
import com.jbrenorv.a4call.feature.room.navigation.openRoomScreen
import com.jbrenorv.a4call.feature.room.navigation.roomScreen

@Composable
fun A4CallNavHost() {
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

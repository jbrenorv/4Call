package com.jbrenorv.acall.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jbrenorv.acall.feature.chat.navigation.chatScreen
import com.jbrenorv.acall.feature.chat.navigation.openChatScreen
import com.jbrenorv.acall.feature.home.navigation.homeScreen
import com.jbrenorv.acall.feature.login.navigation.loginScreen
import com.jbrenorv.acall.feature.room.navigation.openRoomScreen
import com.jbrenorv.acall.feature.room.navigation.roomScreen
import com.jbrenorv.acall.ui.ACallAppState
import kotlinx.serialization.Serializable

@Serializable
data object DummyRoute

@Composable
fun ACallNavHost(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    appState: ACallAppState
) {
    val navController = appState.navController

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = DummyRoute
    ) {
        // TODO: improve this
        composable<DummyRoute> {}

        loginScreen()

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

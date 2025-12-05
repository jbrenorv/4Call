package com.jbrenorv.acall

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jbrenorv.acall.feature.chats.navigation.chatListScreen
import com.jbrenorv.acall.feature.rooms.navigation.roomListScreen
import com.jbrenorv.acall.navigation.HomeNavigationBarDestination

@Composable
fun HomeScreen(
    openRoom: () -> Unit,
    openChat: () -> Unit,
    startDestination: HomeNavigationBarDestination,
    destinations: List<HomeNavigationBarDestination>
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentHierarchy = navBackStackEntry?.destination?.hierarchy

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEach { destination ->
                    val isSelected by remember(currentRoute) {
                        derivedStateOf {
                            currentHierarchy?.any { it.hasRoute(destination.route::class) }
                                ?: false
                        }
                    }
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.iconContentDescription
                            )
                        },
                        label = {
                            Text(
                                text = destination.label
                            )
                        }
                    )
                }
            }
        }
    ) { contentPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            navController = navController,
            startDestination = startDestination.route
        ) {
            roomListScreen(openRoom)
            chatListScreen(openChat)
        }
    }
}

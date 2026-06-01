package com.virtualtrialroom.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.virtualtrialroom.app.ui.theme.VirtualTrialRoomTheme

@Composable
fun VirtualTrialRoomApp() {
    val navController = rememberNavController()
    val navigator = remember(navController) {
        AppNavigator(navController)
    }

    VirtualTrialRoomTheme {
        VirtualTrialRoomNavGraph(
            navController = navController,
            navigator = navigator
        )
    }
}

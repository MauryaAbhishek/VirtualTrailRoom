package com.virtualtrialroom.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.virtualtrialroom.app.ui.screen.AiStylistRoute
import com.virtualtrialroom.app.ui.screen.BeforeAfterRoute
import com.virtualtrialroom.app.ui.screen.CaptureRoute
import com.virtualtrialroom.app.ui.screen.HomeRoute
import com.virtualtrialroom.app.ui.screen.InsightsRoute
import com.virtualtrialroom.app.ui.screen.PreviewRoute
import com.virtualtrialroom.app.ui.screen.ProcessingRoute
import com.virtualtrialroom.app.ui.screen.ResultRoute
import com.virtualtrialroom.app.ui.screen.SavedResultsRoute
import com.virtualtrialroom.app.ui.screen.SettingsRoute
import com.virtualtrialroom.app.ui.screen.ShareCustomerRoute
import com.virtualtrialroom.app.ui.screen.WardrobeRoute

@Composable
fun VirtualTrialRoomNavGraph(
    navController: NavHostController,
    navigator: AppNavigator
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route
    ) {
        composable(AppDestination.Home.route) {
            HomeRoute(
                onCaptureClick = navigator::navigateToCapture,
                onWardrobeClick = navigator::navigateToWardrobe,
                onSavedResultsClick = navigator::navigateToSavedResults,
                onSettingsClick = navigator::navigateToSettings
            )
        }

        composable(AppDestination.Capture.route) {
            CaptureRoute(
                onBackClick = navigator::navigateBack,
                onPhotoCaptured = { photoId ->
                    navigator.navigateToWardrobe(photoId)
                }
            )
        }

        composable(
            route = AppDestination.Wardrobe.route,
            arguments = listOf(
                navArgument(AppDestination.Wardrobe.PHOTO_ID_ARGUMENT) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments
                ?.getString(AppDestination.Wardrobe.PHOTO_ID_ARGUMENT)
            WardrobeRoute(
                onBackClick = navigator::navigateBack,
                onClothingSelected = { clothingId ->
                    if (photoId == null) {
                        navigator.navigateToCapture()
                    } else {
                        navigator.navigateToPreview(
                            photoId = photoId,
                            clothingId = clothingId
                        )
                    }
                }
            )
        }

        composable(
            route = AppDestination.Preview.route,
            arguments = listOf(
                navArgument(AppDestination.Preview.PHOTO_ID_ARGUMENT) {
                    type = NavType.StringType
                },
                navArgument(AppDestination.Preview.CLOTHING_ID_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments
                ?.getString(AppDestination.Preview.PHOTO_ID_ARGUMENT)
                .requireRouteArgument(AppDestination.Preview.PHOTO_ID_ARGUMENT)
            val clothingId = backStackEntry.arguments
                ?.getString(AppDestination.Preview.CLOTHING_ID_ARGUMENT)
                .requireRouteArgument(AppDestination.Preview.CLOTHING_ID_ARGUMENT)

            PreviewRoute(
                photoId = photoId,
                clothingId = clothingId,
                onBackClick = navigator::navigateBack,
                onJobCreated = navigator::navigateToProcessing
            )
        }

        composable(
            route = AppDestination.Processing.route,
            arguments = listOf(
                navArgument(AppDestination.Processing.REQUEST_ID_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments
                ?.getString(AppDestination.Processing.REQUEST_ID_ARGUMENT)
                .requireRouteArgument(AppDestination.Processing.REQUEST_ID_ARGUMENT)

            ProcessingRoute(
                requestId = requestId,
                onResultReady = navigator::navigateToResult
            )
        }

        composable(
            route = AppDestination.Result.route,
            arguments = listOf(
                navArgument(AppDestination.Result.RESULT_ID_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments
                ?.getString(AppDestination.Result.RESULT_ID_ARGUMENT)
                .requireRouteArgument(AppDestination.Result.RESULT_ID_ARGUMENT)

            ResultRoute(
                resultId = resultId,
                onBeforeAfterClick = navigator::navigateToBeforeAfter,
                onShareCustomerClick = navigator::navigateToShareCustomer,
                onBackToHomeClick = {
                    navController.popBackStack(
                        route = AppDestination.Home.route,
                        inclusive = false
                    )
                }
            )
        }

        composable(AppDestination.SavedResults.route) {
            SavedResultsRoute(onBackClick = navigator::navigateBack)
        }

        composable(AppDestination.Settings.route) {
            SettingsRoute(
                onBackClick = navigator::navigateBack,
                onAiStylistClick = navigator::navigateToAiStylist,
                onInsightsClick = navigator::navigateToInsights,
                onShareCustomerClick = navigator::navigateToShareCustomer
            )
        }

        composable(AppDestination.BeforeAfter.route) {
            BeforeAfterRoute(onBackClick = navigator::navigateBack)
        }

        composable(AppDestination.ShareCustomer.route) {
            ShareCustomerRoute(onBackClick = navigator::navigateBack)
        }

        composable(AppDestination.AiStylist.route) {
            AiStylistRoute(onBackClick = navigator::navigateBack)
        }

        composable(AppDestination.Insights.route) {
            InsightsRoute(onBackClick = navigator::navigateBack)
        }
    }
}

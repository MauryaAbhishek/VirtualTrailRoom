package com.virtualtrialroom.app.navigation

import androidx.navigation.NavHostController

class AppNavigator(
    private val navController: NavHostController
) {
    fun navigateToCapture() {
        navController.navigate(AppDestination.Capture.route)
    }

    fun navigateToWardrobe(photoId: String? = null) {
        navController.navigate(AppDestination.Wardrobe.createRoute(photoId))
    }

    fun navigateToPreview(photoId: String, clothingId: String) {
        navController.navigate(AppDestination.Preview.createRoute(photoId, clothingId))
    }

    fun navigateToProcessing(requestId: String) {
        navController.navigate(AppDestination.Processing.createRoute(requestId)) {
            launchSingleTop = true
        }
    }

    fun navigateToResult(resultId: String) {
        navController.navigate(AppDestination.Result.createRoute(resultId)) {
            popUpTo(AppDestination.Home.route)
            launchSingleTop = true
        }
    }

    fun navigateToSavedResults() {
        navController.navigate(AppDestination.SavedResults.route)
    }

    fun navigateToSettings() {
        navController.navigate(AppDestination.Settings.route)
    }

    fun navigateToBeforeAfter() {
        navController.navigate(AppDestination.BeforeAfter.route)
    }

    fun navigateToShareCustomer() {
        navController.navigate(AppDestination.ShareCustomer.route)
    }

    fun navigateToAiStylist() {
        navController.navigate(AppDestination.AiStylist.route)
    }

    fun navigateToInsights() {
        navController.navigate(AppDestination.Insights.route)
    }

    fun navigateBack() {
        navController.navigateUp()
    }
}

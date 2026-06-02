package com.virtualtrialroom.app.navigation

sealed class AppDestination(
    val route: String,
    val title: String
) {
    data object Home : AppDestination("home", "Virtual Trial Room")
    data object Capture : AppDestination("capture", "Capture")
    data object SavedResults : AppDestination("saved-results", "Saved")
    data object Settings : AppDestination("settings", "Settings")
    data object BeforeAfter : AppDestination("before-after", "Before & After")
    data object ShareCustomer : AppDestination("share-customer", "Share")
    data object AiStylist : AppDestination("ai-stylist", "AI Stylist")
    data object Insights : AppDestination("insights", "Insights")

    data object Wardrobe : AppDestination(
        route = "wardrobe?photoId={photoId}",
        title = "Wardrobe"
    ) {
        const val PHOTO_ID_ARGUMENT = "photoId"

        fun createRoute(photoId: String? = null): String {
            return if (photoId.isNullOrBlank()) {
                "wardrobe"
            } else {
                "wardrobe?photoId=${photoId.encodeRouteSegment()}"
            }
        }
    }

    data object Preview : AppDestination(
        route = "preview/{photoId}/{clothingId}",
        title = "Preview"
    ) {
        const val PHOTO_ID_ARGUMENT = "photoId"
        const val CLOTHING_ID_ARGUMENT = "clothingId"

        fun createRoute(photoId: String, clothingId: String): String {
            return "preview/${photoId.encodeRouteSegment()}/${clothingId.encodeRouteSegment()}"
        }
    }

    data object Processing : AppDestination(
        route = "processing/{requestId}",
        title = "Generating"
    ) {
        const val REQUEST_ID_ARGUMENT = "requestId"

        fun createRoute(requestId: String): String {
            return "processing/${requestId.encodeRouteSegment()}"
        }
    }

    data object Result : AppDestination(
        route = "result/{resultId}",
        title = "Result"
    ) {
        const val RESULT_ID_ARGUMENT = "resultId"

        fun createRoute(resultId: String): String {
            return "result/${resultId.encodeRouteSegment()}"
        }
    }
}

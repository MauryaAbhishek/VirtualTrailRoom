package com.virtualtrialroom.app.navigation

import android.net.Uri

fun String.encodeRouteSegment(): String {
    return Uri.encode(this)
}

fun String?.requireRouteArgument(argumentName: String): String {
    return requireNotNull(this) {
        "Missing required navigation argument: $argumentName"
    }
}


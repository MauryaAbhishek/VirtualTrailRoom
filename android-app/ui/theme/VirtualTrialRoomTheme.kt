package com.virtualtrialroom.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Mist,
    primaryContainer = Cloud,
    onPrimaryContainer = Ink,
    secondary = Green,
    onSecondary = Mist,
    error = Red,
    background = Mist,
    onBackground = Ink,
    surface = Mist,
    onSurface = Ink,
    surfaceVariant = Cloud,
    onSurfaceVariant = Slate
)

private val DarkColors = darkColorScheme(
    primary = Blue,
    onPrimary = Mist,
    primaryContainer = BlueDark,
    onPrimaryContainer = Mist,
    secondary = Green,
    onSecondary = Mist,
    error = Red,
    background = Ink,
    onBackground = Mist,
    surface = Ink,
    onSurface = Mist,
    surfaceVariant = Slate,
    onSurfaceVariant = Cloud
)

@Composable
fun VirtualTrialRoomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}


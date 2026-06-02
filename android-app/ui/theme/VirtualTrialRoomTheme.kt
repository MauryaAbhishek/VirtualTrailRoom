package com.virtualtrialroom.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Rose,
    onPrimary = Porcelain,
    primaryContainer = Color(0xFFFFD9E6),
    onPrimaryContainer = Ink,
    secondary = Teal,
    onSecondary = Porcelain,
    secondaryContainer = Color(0xFFD6F3F1),
    onSecondaryContainer = Ink,
    tertiary = Saffron,
    onTertiary = Ink,
    error = Red,
    background = Mist,
    onBackground = Ink,
    surface = Porcelain,
    onSurface = Ink,
    surfaceVariant = Cloud,
    onSurfaceVariant = Slate
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF8FBC),
    onPrimary = Ink,
    primaryContainer = RoseDark,
    onPrimaryContainer = Porcelain,
    secondary = Color(0xFF73D8D0),
    onSecondary = Ink,
    secondaryContainer = Color(0xFF095B63),
    onSecondaryContainer = Porcelain,
    tertiary = Saffron,
    onTertiary = Ink,
    error = Red,
    background = Ink,
    onBackground = Porcelain,
    surface = Color(0xFF171B22),
    onSurface = Porcelain,
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

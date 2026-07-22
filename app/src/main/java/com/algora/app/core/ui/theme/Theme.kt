package com.algora.app.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = LightSurface,
    secondary = Blue,
    onSecondary = LightSurface,
    tertiary = Violet,
    onTertiary = LightSurface,
    background = LightBackground,
    onBackground = Color(0xFF161A22),
    surface = LightSurface,
    onSurface = Color(0xFF161A22),
    onSurfaceVariant = LightMutedText,
    outline = LightBorder,
    outlineVariant = LightBorderSubtle,
)

private val DarkColors = darkColorScheme(
    primary = Indigo,
    onPrimary = DarkBackground,
    secondary = Blue,
    onSecondary = DarkBackground,
    tertiary = Violet,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = LightSurface,
    surface = DarkSurface,
    onSurface = LightSurface,
    onSurfaceVariant = DarkMutedText,
    outline = DarkBorder,
    outlineVariant = DarkBorder,
)

val LocalCategoryAccents = staticCompositionLocalOf { CategoryAccents.all }

@Composable
fun AlgoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(LocalCategoryAccents provides CategoryAccents.all) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AlgoraTypography,
            content = content,
        )
    }
}

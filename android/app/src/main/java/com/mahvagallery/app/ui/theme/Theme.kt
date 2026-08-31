package com.mahvagallery.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryDark,
    secondary = TextDark,
    background = BgLight,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = TextDark,
    onSurface = TextDark
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = TextLightDark,
    background = BgDark,
    surface = SurfaceDark,
    onPrimary = White,
    onSecondary = White,
    onBackground = TextLightDark,
    onSurface = TextLightDark
)

@Composable
fun MahvaGalleryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = (if (darkTheme) BgDark else PrimaryDark).toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

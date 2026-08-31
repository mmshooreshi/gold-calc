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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    fontScaleDelta: Int = 0,
    isBoldText: Boolean = false,
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

    val scaleRatio = (14f + fontScaleDelta) / 14f

    val scaledTypography = Typography.copy(
        titleLarge = Typography.titleLarge.copy(
            fontSize = (22 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Black else FontWeight.Bold
        ),
        titleMedium = Typography.titleMedium.copy(
            fontSize = (18 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Black else FontWeight.Bold
        ),
        titleSmall = Typography.titleSmall.copy(
            fontSize = (15 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Black else FontWeight.SemiBold
        ),
        bodyLarge = Typography.bodyLarge.copy(
            fontSize = (15 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal
        ),
        bodyMedium = Typography.bodyMedium.copy(
            fontSize = (14 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal
        ),
        bodySmall = Typography.bodySmall.copy(
            fontSize = (12 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal
        ),
        labelLarge = Typography.labelLarge.copy(
            fontSize = (14 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Medium
        ),
        labelMedium = Typography.labelMedium.copy(
            fontSize = (12 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal
        ),
        labelSmall = Typography.labelSmall.copy(
            fontSize = (10 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}

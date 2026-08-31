package com.mahvagallery.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

data class AppCustomColors(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val border: Color,
    val borderLight: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val inputBg: Color,
    val inputBgDisabled: Color,
    val inputBorder: Color,
    val primary: Color,
    val primaryDark: Color,
    val primaryAccent: Color,
    val lockedBg: Color,
    val lockedBorder: Color,
    val lockedText: Color,
    val cardBg: Color,
    val divider: Color,
    val highlightBg: Color,
    val highlightBorder: Color,
    val success: Color,
    val danger: Color,
    val warning: Color
)

val LightAppColors = AppCustomColors(
    isDark = false,
    background = Color(0xFFF1F5F9),       // Modern cool slate light
    surface = Color(0xFFFFFFFF),          // Pure white
    surfaceElevated = Color(0xFFFFFFFF),
    border = Color(0xFFE2E8F0),           // Soft border
    borderLight = Color(0xFFF1F5F9),
    textPrimary = Color(0xFF0F172A),      // Crisp high-contrast navy/slate
    textSecondary = Color(0xFF334155),
    textMuted = Color(0xFF64748B),
    inputBg = Color(0xFFFFFFFF),
    inputBgDisabled = Color(0xFFF8FAFC),
    inputBorder = Color(0xFFCBD5E1),
    primary = Color(0xFF172051),          // Royal Navy
    primaryDark = Color(0xFF0F172A),
    primaryAccent = Color(0xFFD97706),    // Gold Amber
    lockedBg = Color(0xFFFEF3C7),         // Warm golden tint
    lockedBorder = Color(0xFFF59E0B),     // Vivid gold border
    lockedText = Color(0xFF92400E),       // Rich dark gold text
    cardBg = Color(0xFFFFFFFF),
    divider = Color(0xFFE2E8F0),
    highlightBg = Color(0xFFEEF2FF),
    highlightBorder = Color(0xFF6366F1),
    success = Color(0xFF10B981),
    danger = Color(0xFFEF4444),
    warning = Color(0xFFF59E0B)
)

val DarkAppColors = AppCustomColors(
    isDark = true,
    background = Color(0xFF0B0F19),       // Ultra-deep luxury OLED dark
    surface = Color(0xFF131B2E),          // Rich dark slate
    surfaceElevated = Color(0xFF1A243D),
    border = Color(0xFF263554),           // High-contrast clean dark border
    borderLight = Color(0xFF1C2740),
    textPrimary = Color(0xFFF8FAFC),      // Crisp pure white text
    textSecondary = Color(0xFFE2E8F0),
    textMuted = Color(0xFF94A3B8),        // Clear readable secondary
    inputBg = Color(0xFF18223B),          // Contrasted dark input
    inputBgDisabled = Color(0xFF0F172A),
    inputBorder = Color(0xFF334468),
    primary = Color(0xFF6366F1),          // Vibrant luxury indigo
    primaryDark = Color(0xFF818CF8),
    primaryAccent = Color(0xFFFBBF24),    // Glowing gold
    lockedBg = Color(0xFF2D200E),         // High-contrast dark amber
    lockedBorder = Color(0xFFF59E0B),
    lockedText = Color(0xFFFCD34D),       // Luminous gold
    cardBg = Color(0xFF131B2E),
    divider = Color(0xFF263554),
    highlightBg = Color(0xFF1E293B),
    highlightBorder = Color(0xFF818CF8),
    success = Color(0xFF34D399),
    danger = Color(0xFFF87171),
    warning = Color(0xFFFBBF24)
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }
val LocalAppFontScale = compositionLocalOf { 1.0f }

object AppTheme {
    val colors: AppCustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val fontScale: Float
        @Composable
        @ReadOnlyComposable
        get() = LocalAppFontScale.current
}

@Composable
fun scaledSp(base: Float): TextUnit {
    return (base * LocalAppFontScale.current).sp
}

@Composable
fun scaledSp(base: Int): TextUnit {
    return (base.toFloat() * LocalAppFontScale.current).sp
}

private val LightColorScheme = lightColorScheme(
    primary = LightAppColors.primary,
    secondary = LightAppColors.primaryAccent,
    background = LightAppColors.background,
    surface = LightAppColors.surface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightAppColors.textPrimary,
    onSurface = LightAppColors.textPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkAppColors.primary,
    secondary = DarkAppColors.primaryAccent,
    background = DarkAppColors.background,
    surface = DarkAppColors.surface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkAppColors.textPrimary,
    onSurface = DarkAppColors.textPrimary
)

@Composable
fun MahvaGalleryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontScaleDelta: Int = 0,
    isBoldText: Boolean = false,
    content: @Composable () -> Unit
) {
    val customColors = if (darkTheme) DarkAppColors else LightAppColors
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = customColors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Dynamic Multiplier: delta 0 = 1.0x, -4 = 0.75x, +10 = 1.65x
    val scaleRatio = (14f + fontScaleDelta).coerceIn(9f, 26f) / 14f

    val scaledTypography = Typography.copy(
        titleLarge = Typography.titleLarge.copy(
            fontSize = (22 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Black else FontWeight.Bold,
            color = customColors.textPrimary
        ),
        titleMedium = Typography.titleMedium.copy(
            fontSize = (18 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Black else FontWeight.Bold,
            color = customColors.textPrimary
        ),
        titleSmall = Typography.titleSmall.copy(
            fontSize = (15 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Black else FontWeight.SemiBold,
            color = customColors.textPrimary
        ),
        bodyLarge = Typography.bodyLarge.copy(
            fontSize = (15 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal,
            color = customColors.textPrimary
        ),
        bodyMedium = Typography.bodyMedium.copy(
            fontSize = (14 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal,
            color = customColors.textPrimary
        ),
        bodySmall = Typography.bodySmall.copy(
            fontSize = (12 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal,
            color = customColors.textMuted
        ),
        labelLarge = Typography.labelLarge.copy(
            fontSize = (14 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Medium,
            color = customColors.textPrimary
        ),
        labelMedium = Typography.labelMedium.copy(
            fontSize = (12 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal,
            color = customColors.textSecondary
        ),
        labelSmall = Typography.labelSmall.copy(
            fontSize = (10 * scaleRatio).sp,
            fontWeight = if (isBoldText) FontWeight.Bold else FontWeight.Normal,
            color = customColors.textMuted
        )
    )

    CompositionLocalProvider(
        LocalAppColors provides customColors,
        LocalAppFontScale provides scaleRatio
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = scaledTypography,
            content = content
        )
    }
}

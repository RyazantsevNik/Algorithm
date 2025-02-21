package com.example.algorithms.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = SoftBlue,
    onPrimaryContainer = DarkBlue,
    secondary = LightBlue,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    surfaceVariant = Color.White,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF37474F), // Темно-синий
    secondary = Color(0xFF546E7A), // Серо-синий
    tertiary = Color(0xFF263238), // Очень темный синий
    background = Color(0xFF1C2833), // Темный фон
    surface = Color(0xFF263238), // Поверхность
    onPrimary = Color.White, // Текст на основном цвете
    onSecondary = Color.White, // Текст на вторичном цвете
    onBackground = Color.White, // Текст на фоне
    onSurface = Color.White // Текст на поверхности
)

@Composable
fun AlgorithmsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> LightColorScheme
    }

    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
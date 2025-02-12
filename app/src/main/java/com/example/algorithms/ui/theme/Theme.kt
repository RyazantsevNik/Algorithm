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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Основной цвет
    secondary = Color(0xFF3700B3), // Вторичный цвет
    tertiary = Color(0xFF03DAC5), // Акцентный цвет
    background = Color(0xFFF5F5F5), // Фон
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
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

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
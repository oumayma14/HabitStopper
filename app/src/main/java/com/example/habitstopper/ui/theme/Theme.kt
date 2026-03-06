package com.example.habitstopper.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    background = Color(0xFFF7F4F0),
    surface = Color.White,
    primary = Color(0xFF6C63FF),
    secondary = Color(0xFF00D4AA),
    onBackground = Color(0xFF1A1A2E),
    onSurface = Color(0xFF1A1A2E),
)

private val DarkColors = darkColorScheme(
    background = Color(0xFF0D0D1A),
    surface = Color(0xFF1A1A2E),
    primary = Color(0xFF6C63FF),
    secondary = Color(0xFF00D4AA),
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun HabitStopperTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
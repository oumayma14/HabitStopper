package com.example.habitstopper.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector ? = null
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Habits : BottomNavItem("habits", "Habits", Icons.Default.List)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)

    object Profile : BottomNavItem("profile", "Profile", icon = null)
}
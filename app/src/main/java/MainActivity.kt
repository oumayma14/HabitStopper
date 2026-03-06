package com.example.habitstopper

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitstopper.ui.theme.HabitStopperTheme
import com.example.habitstopper.navigation.BottomBar
import com.example.habitstopper.navigation.BottomNavItem
import com.example.habitstopper.screens.SettingsScreen
import com.example.habitstopper.screens.ProfileScreen
import com.example.habitstopper.screens.LoginScreen
import com.example.habitstopper.screens.SignUpScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habitstopper.screens.HomeScreen
import com.example.habitstopper.screens.HelpScreen
import com.example.habitstopper.screens.FeedbackScreen
import com.example.habitstopper.screens.TermsScreen
import com.example.habitstopper.screens.PrivacyScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val userViewModel: UserViewModel = viewModel()

    // load user profile to get dark mode preference
    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
    }

    val userProfile = userViewModel.userProfile
    val isDarkMode = userProfile?.darkMode ?: false

    val showBottomBar = currentRoute in listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Profile.route,
        BottomNavItem.Settings.route,
        BottomNavItem.Habits.route
    )

    HabitStopperTheme(darkTheme = isDarkMode) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(navController)
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") { LoginScreen(navController) }
                composable("signup") { SignUpScreen(navController) }
                composable(BottomNavItem.Home.route) { HomeScreen()}
                composable(BottomNavItem.Settings.route) { SettingsScreen(navController) }
                composable(BottomNavItem.Profile.route) { ProfileScreen(navController) }
                composable("help") { HelpScreen(navController) }
                composable("feedback") { FeedbackScreen(navController) }
                composable("terms") { TermsScreen(navController) }
                composable("privacy") { PrivacyScreen(navController) }
            }
        }
    }
}
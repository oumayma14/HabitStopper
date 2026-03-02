package com.example.habitstopper

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitstopper.ui.theme.HabitStopperTheme
import com.example.habitstopper.navigation.BottomBar
import com.example.habitstopper.navigation.BottomNavItem
import com.example.habitstopper.screens.HabitsScreen
import com.example.habitstopper.screens.SettingsScreen
import com.example.habitstopper.screens.ProfileScreen
import com.example.habitstopper.screens.LoginScreen
import com.example.habitstopper.screens.SignUpScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habitstopper.screens.HomeScreen
data class HabitCard(
    val name: String,
    val icon: ImageVector,
    val cardColor: Color
)

val habits = listOf(
    HabitCard("No Sugar", Icons.Filled.LocalCafe, Color(0xFFFFF3E0)),
    HabitCard("No Smoking", Icons.Filled.Whatshot, Color(0xFFFFEBEE)),
    HabitCard("No DoomScrolling", Icons.Filled.Smartphone, Color(0xFFE3F2FD))
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitItemCard(
    habit: HabitCard,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = habit.cardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = habit.icon,
                    contentDescription = habit.name
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(habit.name)
            }

            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HabitStopperTheme {
                MainScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Profile.route,
        BottomNavItem.Settings.route,
        BottomNavItem.Habits.route
    )
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login"){ LoginScreen(navController)}
            composable("signup"){ SignUpScreen(navController)}
            composable(BottomNavItem.Home.route) { HomeScreen(navController) }
            composable(BottomNavItem.Habits.route) { HabitsScreen() }
            composable(BottomNavItem.Settings.route) { SettingsScreen() }
            composable(BottomNavItem.Profile.route) {ProfileScreen()}
        }
    }
}
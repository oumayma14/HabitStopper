package com.example.habitstopper.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.habitstopper.R

@Composable
fun BottomBar(
    navController: NavController,
    userImageUrl: String? = null // pass null to use default image
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Habits,
        BottomNavItem.Settings,
        BottomNavItem.Profile
    )

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value
            ?.destination
            ?.route

        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if (item == BottomNavItem.Profile) {
                        val avatarModifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .then(
                                if (selected) Modifier.border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                ) else Modifier
                            )

                        if (!userImageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = userImageUrl,
                                contentDescription = "Profile",
                                modifier = avatarModifier
                            )
                        } else {
                            // default image in drawable (create this)
                            Image(
                                painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_profile_placeholder),
                                contentDescription = "Profile",
                                modifier = avatarModifier
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon!!,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) }
            )
        }
    }
}